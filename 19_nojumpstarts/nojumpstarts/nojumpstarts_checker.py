#!/usr/bin/env python3
# -*- coding: utf-8 -*-
# ~/Android/Sdk/tools/bin/avdmanager list avd
# ~/Android/Sdk/tools/avdmanager create avd --force --name mobiotsec --abi google_apis/x86 --package 'system-images;android-26;google_apis;x86'
# ~/Android/Sdk/emulator/emulator -avd mobiotsec -no-audio -no-boot-anim -accel on -gpu swiftshader_indirect &

import argparse
import os
import shutil
import time
import subprocess as subp
from androguard.core.bytecodes.apk import APK

def parse_logs():
    with open('./nojumpstarts_logs.txt') as f:
        print(f.read())

def print_logs():
    f = open("nojumpstarts_logs.txt", "w")
    subp.call(["adb", "logcat", "-c"])
    try:
        subp.call(["adb", "logcat", "-s", "MOBIOTSEC"], stdout=f, timeout=3)
    except subp.TimeoutExpired:
        parse_logs()

def launch_malicious_app(apk):
    print("Launching the malicious app")
    mainactivity = "{}/{}".format(apk.get_package(), apk.get_main_activity())
    os.system("adb shell am start -n {act}".format(act=mainactivity))

def launch_victim_app(apk):
    print("Launching the victim app")
    mainactivity = "{}/{}".format(apk.get_package(), apk.get_main_activity())
    os.system("adb shell am start -n {act} --es {key} {flag}".format(act=mainactivity, key="flag", flag="FLAG{virtus_unita_fortior}"))

def uninstall(apk):
    if (os.system("adb shell pm list packages | grep {package}".format(package=apk.get_package())) == 0):
        print("Uninstalling the app")
        subp.call(["adb", "uninstall", apk.get_package()], stdout=subp.DEVNULL)

def install(apk):
    print("Installing the app")
    while True:
        try:
            os.system("adb install -g {apk}".format(apk=apk.get_filename()))
            break
        except subp.CalledProcessError as err:
            print('[!] install failed')
            print(err)
            print('[!] retrying')

def parse_args():
    parser = argparse.ArgumentParser()
    parser.add_argument("victimapp_apk_path", help="path to the victim app apk file")
    parser.add_argument("malapp_apk_path", help="path to the malicious app apk file")
    args = parser.parse_args()
    return args

def main(args):
    print("Launching the emulator")
    os.system("~/Android/Sdk/emulator/emulator -avd mobiotsec -no-audio -no-boot-anim -accel on -gpu swiftshader_indirect &")
    time.sleep(3)
    victimApk = APK(args.victimapp_apk_path)
    malApk = APK(args.malapp_apk_path)
    uninstall(victimApk)
    install(victimApk)
    uninstall(malApk)
    install(malApk)
    launch_victim_app(victimApk)
    time.sleep(2)
    launch_malicious_app(malApk)
    #time.sleep(2)
    print_logs()

if __name__ == "__main__":
    main(parse_args())
