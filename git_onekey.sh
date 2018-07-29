#!/bin/bash
# @changelog Add remote push action, update git status, add log print

#set -e
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

function commitCode() {
	echo
	
	code_status="$1"
	file="$2"
	comment="$3"
	
	log "_black" "git add $file"
	if [ $debug_mode == "false" ]; then
		git add "$file"
		#echo "[$debug_mode]"
	fi
	
	if [ -z $comment ]; then
		log "red" "commit file ==> $file"
		
		git diff $file
		echo
		read -p "input commit comment message: " comment
	fi
	
	case $code_status in
	    "M"|"MM")  
			log "_blue" "Changed file ==> $file"
			if [ $debug_mode == "false" ]; then
	    		git commit -m ":sparkles: :bento: :recycle: $emoji Changed ${comment}"
	    	fi
	    ;;
	    "R"|"RM")  
			log "_yellow" "Renamed file ==> $file"
			if [ $debug_mode == "false" ]; then
	    		git commit -m ":sparkles: :bento: :truck: $emoji Renamed ${comment}"
	    	fi
	    ;;
	    "A")  
			log "_green" "Added file ==> $file"
			if [ $debug_mode == "false" ]; then
	    		git commit -m ":sparkles: :bento: $emoji Added ${comment}"
	    	fi
	    ;;
	    "D")  
			log "_red" "Removed file ==> $file"
			if [ $debug_mode == "false" ]; then
	    		git commit -m ":sparkles: :bento: :fire: $emoji Removed ${comment}"
	    	fi
	    ;;
	    "C")  
			log "_sky_blue" "Added copy file ==> $file"
			if [ $debug_mode == "false" ]; then
	    		git commit -m ":sparkles: :bento: $emoji Added ${comment}"
	    	fi
	    ;;
	    "U")  
			log "_white" "Added copy file ==> $file"
			if [ $debug_mode == "false" ]; then
	    		git commit -m ":sparkles: :bento: :recycle: $emoji Updated ${comment}"
	    	fi
	    ;;
	    "??")  
			log "_purple" "First init add ==> $file"
			if [ $debug_mode == "false" ]; then
	    		git commit -m ":sparkles: :bento: :tada: $emoji Init add ${comment}"
	    	fi
	    ;;
	    *)  
	    	log "rrr" "===================================> NOT FOUND Match Status：$code_status"
	    ;;
	esac
}

function findCommitFiles() {
	# git status -su | awk -F ' ' '{print $1}'
	
	IFS_OLD=$IFS
	IFS=$'\n'
	file_status=`git status -su`
	
	for status in $file_status; do
		log "_line" "                                                                                                                                "
		log "_white" "file status ==> ${status}"
		
		state=`echo $status | awk -F ' ' '{print $1}'`
		file=`echo $status | awk -F ' ' '{print $2}'`
		
		if [ $state == "RM" ]; then
			file=`echo $status | awk -F ' -> ' '{print $2}'`
		fi
		
		#echo "status --> ${state}"
		#echo "file --> ${file}"
		fetchComment $file
		fetchCommentStatus $comment
		commitCode $state $file $comment
	done
	
	IFS=$IFS_OLD
}

function fetchComment() {
	file="$1"
	#echo "read file ==> $file"
	#grep -A 3 -m 3 " \* " $file
	
	keyword="@changelog"
	defaultKeyword=" \* "

	#head -100 "$file"
	#comment=`sed -n '1,100p' $file | grep -iw -m 1 "$keyword" | sed "s/$keyword//g"`
	#comment=`grep -iw -m 1 "$keyword" $file | sed "s/$keyword//g"`
	comment=`grep -iw -m 1 "$keyword" $file | awk -F "$keyword" '{print $2}'`
	if [ -z $comment ]; then
		comment=`grep -i -m 1 "$defaultKeyword" $file`
	fi
	
	# replace space
	comment=`echo $comment | sed "s/$defaultKeyword//g" | sed 's/^ //g'`
	
	unset emoji
	if [ -z $comment ]; then
		generatorComment
	fi
	
	echo "comment ==> $comment"
}

function generatorComment() {
	log "red" " generator comment =========> ${file##*.}"
	
	suffix=${file##*.}
	case $suffix in
		"gitignore")
			comment="configure git 'gitignore' to ignore some files"
			emoji=":see_no_evil:"
		;;
		"properties"|"xml")
			emoji=":wrench:"
		;;
		*)
			unset emoji
			unset comment
			#comment=""
		;;
	esac	
}

# fetch comment content status<add/remove/modify/update/rename/delete>
function fetchCommentStatus() {
	
	comment="$1"
	comment_state=`echo $comment | awk -F ' ' '{print $1}'`
	
	if [ -n $comment_state ]; then
		case $comment_state in
			add|Add|added|Added)
				fetch_status="A"
			;;
			remove|Remove|removed|Removed)
				fetch_status="D"
			;;
			modify|Modify|modified|Modified)
				fetch_status="M"
			;;
			update|Update|updated|Updated)
				fetch_status="M"
			;;
			rename|Rename|renamed|Renamed)
				fetch_status="R"
			;;
			delete|Delete|deleted|Deleted)
				fetch_status="D"
			;;
			*)
				unset fetch_status
			;;
		esac
		
		# 截掉首单词
		if [ $fetch_status ]; then
			comment=`echo $comment | sed "s/^$comment_state//"`
			state=$fetch_status
		fi
	fi
	
	# echo "comment_state==========> $comment_state"
	# echo "state==========> $fetch_status"
	# echo "comment==========> $comment"
}

function pushGit() {
	git status
	
	git push origin master
}

# setup shell
function setup() {
	log "red" "================ Start 'Add & Commit' code to local repository ================"
	findCommitFiles
	
	if [ $push == "true" ]; then
		printf "\n"
		log "red" "================   Start 'pushing' code to remote repository   ================"
		pushGit
	fi
	
	echo
	echo
	log "green" " Done!!!"
}
	
debug_mode="false"
push="false"	
for param in "$@"; do
    log "green" "====> 参数: $param"
    if [ $param == "-d" -o $param == "--debug" ]; then
    	debug_mode="true"
    fi
    if [ $param == "-p" -o $param == "--push" ]; then
    	push="true"
    fi
done
	
setup



