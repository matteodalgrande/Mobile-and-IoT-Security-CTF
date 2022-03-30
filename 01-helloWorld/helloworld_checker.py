#!/usr/bin/env python3
# -*- coding: utf-8 -*-
# ~/Android/Sdk/tools/bin/avdmanager list avd
# ~/Android/Sdk/tools/avdmanager create avd --force --name mobiotsec --abi google_apis/x86 --package 'system-images;android-26;google_apis;x86'
# ~/Android/Sdk/emulator/emulator -avd mobiotsec -no-audio -no-boot-anim -accel on -gpu swiftshader_indirect &

# TODO check if the emulator is already running

import argparse
import os
import shutil
import time
import subprocess as subp
from androguard.core.bytecodes.apk import APK

def parse_logs():
    with open('./helloworld.txt') as f:
        if 'hello-world-mobiotsec' in f.read():
            print("Good job! The flag is FLAG{veni_vidi_vici}")
        else:
            print("Something went wrong!")

def print_logs(apk):
    f = open("helloworld.txt", "w")
    subp.call(["adb", "logcat", "-c"])
    try:
        subp.call(["adb", "logcat", "-s", "MOBIOTSEC"], stdout=f, timeout=3)
    except subp.TimeoutExpired:
        parse_logs()

def launch_app(apk):
    print("Lauching the app")
    mainactivity = "{}/{}".format(apk.get_package(), apk.get_main_activity())
    os.system("adb shell am start -n {act}".format(act=mainactivity))

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
    parser.add_argument("apk_path", help="path to app apk file")
    args = parser.parse_args()
    return args

def main(args):
    print("Lauching the emulator")
    os.system("~/Android/Sdk/emulator/emulator -avd mobiotsec -no-audio -no-boot-anim -accel on -gpu swiftshader_indirect &")
    time.sleep(3)
    apk = APK(args.apk_path)
    uninstall(apk)
    install(apk)
    launch_app(apk)
    print_logs(apk)

if __name__ == "__main__":
    main(parse_args())
