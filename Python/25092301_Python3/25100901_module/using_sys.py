#!/usr/bin/python3
# 文件名: using_sys.py


import sys

print('命令行参数如下:')
for i in sys.argv:
    print(i)

print('\nPython 路径为：', sys.path, '\n')