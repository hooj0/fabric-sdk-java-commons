#!/bin/bash

set -e
#set -o pipefail
trap "echo 'error: Script failed: see failed command above'" ERR

function help() {
	echo "git onekey auto commit code"
}

function log() {
	# 字颜色：30—–37
	# 字背景颜色范围：40—–47
	case $1 in
		"red")
			echo -e "\033[31m$2\033[0m" # 红色字
		;; 
		"yellow")
			echo -e "\033[33m$2\033[0m" # 黄色字
		;; 
		"green")
			echo -e "\033[32m$2\033[0m" # 绿色字
		;; 
		"blue")
			echo -e "\033[34m$2\033[0m" # 蓝色字
		;; 
		"purple")
			echo -e "\033[35m$2\033[0m" # 紫色字
		;; 
		"sky_blue")
			echo -e "\033[36m$2\033[0m" # 天蓝字
		;; 
		"white")
			echo -e "\033[37m$2\033[0m" # 白色字
		;; 
		"_black")
			echo -e "\033[40;37m $2 \033[0m" # 黑底白字
		;; 
		"_red")
			echo -e "\033[41;30m $2 \033[0m" # 红底黑字
		;; 
		"_yellow")
			echo -e "\033[43;30m $2 \033[0m" # 黄底黑字
		;; 
		"_green")
			echo -e "\033[42;30m $2 \033[0m" # 绿底黑字
		;; 
		"_blue")
			echo -e "\033[44;30m $2 \033[0m" # 蓝底黑字
		;; 
		"_purple")
			echo -e "\033[45;30m $2 \033[0m" # 紫底黑字
		;; 
		"_sky_blue")
			echo -e "\033[46;30m $2 \033[0m" # 天蓝底黑字
		;; 
		"_white")
			echo -e "\033[47;30m $2 \033[0m" # 白底黑字
		;; 
		"_line")
			echo -e "\033[4;31m $2 \033[0m" # 下划线红字
		;; 
		"rrr")
			echo -e "\033[5;34m $2 \033[0m" # 红字在闪烁
		;; 
		*)
			echo "$2"
		;;
	esac
}

function commitCode() {
	#echo "##########################################################"
	#echo "#####         git commit file & comment          #########"
	#echo "##########################################################"
	echo
	
	code_status="$1"
	file="$2"
	comment="$3"
	
	log "_black" "git add $file"
	git add "$file"
	
	if [ -z $comment ]; then
		log "red" "commit file ==> $file"
		read -p "input commit comment message: " comment
	fi
	
	case $code_status in
	    "M")  
			log "_blue" "Changed file ==> $file"
	    	git commit -m ":sparkles::bento::recycle: Changed ${comment}"
	    ;;
	    "R")  
			log "_yellow" "Renamed file ==> $file"
	    	git commit -m ":sparkles::bento::truck: Renamed ${comment}"
	    ;;
	    "A")  
			log "_green" "Added file ==> $file"
	    	git commit -m ":sparkles::bento: Added ${comment}"
	    ;;
	    "D")  
			log "_red" "Removed file ==> $file"
	    	git commit -m ":sparkles::bento::fire: Removed ${comment}"
	    ;;
	    "C")  
			log "_sky_blue" "Added copy file ==> $file"
	    	git commit -m ":sparkles::bento: Added ${comment}"
	    ;;
	    "U")  
			log "_white" "Added copy file ==> $file"
	    	git commit -m ":sparkles::bento::recycle: Updated ${comment}"
	    ;;
	    "??")  
			log "_purple" "First init add ==> $file"
	    	git commit -m ":sparkles::tada::bento: Init add ${comment}"
	    ;;
	    *)  
	    	log "rrr" "没有匹配的状态：$code_status"
	    ;;
	esac
}

function findCommitFile() {
	echo
	#echo "##########################################################"
	#echo "#####           find git commit files            #########"
	#echo "##########################################################"
	#echo
	
	# git status -s | awk -F ' ' '{print $1}'
	
	IFS_OLD=$IFS
	IFS=$'\n'
	file_status=$(git status -s)
	
	for status in $file_status; do
		log "_line" "                                                                                                                                "
		log "_white" "file status ==> ${status}"
		
		state=`echo $status | awk -F ' ' '{print $1}'`
		file=`echo $status | awk -F ' ' '{print $2}'`
		
		#echo "status --> ${state}"
		#echo "file --> ${file}"
		readerComment $file
		
		commitCode $state $file $comment
		# printf "\n"
	done
	
	IFS=$IFS_OLD
}

function readerComment() {
	#echo "##########################################################"
	#echo "#####       read git commit file comment         #########"
	#echo "##########################################################"
	#echo
	
	file="$1"
	
	# echo "read file ==> $file"
	
	# $ grep -B 3 " \* " $file
	comment=`egrep -m 3 " \* " $file | sed s/\*//g | sed 's/^ //g'`
	
	echo "comment ==> $comment"
}
	
function colors() {
	log "red" "22222222"
	log "yellow" "22222222"
	log "green" "22222222"
	log "blue" "22222222"
	log "purple" "22222222"
	log "sky_blue" "22222222"
	log "white" "22222222"
	
	log "_red" "22222222"
	log "_yellow" "22222222"
	log "_green" "22222222"
	log "_blue" "22222222"
	log "_purple" "22222222"
	log "_sky_blue" "22222222"
	log "_white" "22222222"
	log "_black" "22222222"
}

findCommitFile

