# coding=utf-8  
# !/usr/bin/python

import os
import os.path
import shutil
import sys
import string  
import fnmatch
import pickle
import time

def iter_find_files(path, fnexp):
	for root, dirs, files, in os.walk(path):
		for filename in fnmatch.filter(files, fnexp):
			yield os.path.join(root, filename)

def main():
	cwddir = os.path.split(os.path.realpath(__file__))[0];
	srcdir = cwddir + "/proto/"
	destdir = cwddir + "/src/"

	try:
		l = pickle.load(open("db",'rb'))
	except IOError:
		l = []

	db = dict(l)

	print(srcdir)
	print(destdir)

	files = ""
	for filename in iter_find_files(srcdir, "*.proto"):
		mtime = time.ctime(os.path.getmtime(filename))
		if db.get(filename, None) != mtime:
			cmd = cwddir + "\\" + "protoc.exe --proto_path=\"%s\" --java_out=%s %s" % (srcdir, destdir, filename)
			print(cmd)
			db[filename] = mtime
			os.system(cmd)

	pickle.dump(list(db.items()), open("db", "wb+"))
	print("Over")

if __name__ == '__main__':
	main()
	os.system("pause")
