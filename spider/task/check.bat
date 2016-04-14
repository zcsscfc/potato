::这个批处理用来供windows任务计划定时调用执行python脚本，检查数据源
@echo off
G:
cd G:\gitpotato\potato\spider\common
start pythonw BaseCrawler.py
exit