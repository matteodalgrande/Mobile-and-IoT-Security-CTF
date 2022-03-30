#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import argparse
import os
import subprocess as subp
from androguard.core.bytecodes.apk import APK

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
            os.system("adb install -g {apk}".format(apk=apk.get_filename())) # adb install -g (-g: grant all runtime permissions)
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
    apk = APK(args.apk_path)
    uninstall(apk)
    install(apk)
    launch_app(apk)

if __name__ == "__main__":
    main(parse_args())
