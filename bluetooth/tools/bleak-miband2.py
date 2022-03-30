import platform
import asyncio
import logging
​
from Crypto.Cipher import AES
from Crypto import Random
​
from bleak import BleakClient
from bleak import discover
​
newdata = 0
​
async def run(loop, debug=False):
    log = logging.getLogger(__name__)
    if debug:
        import sys
​
        loop.set_debug(True)
        log.setLevel(logging.DEBUG)
        h = logging.StreamHandler(sys.stdout)
        h.setLevel(logging.DEBUG)
        log.addHandler(h)
​
    async def scan_ble_devices():
        devices = await discover()
        for d in devices:
            print(d)
        return devices
​
    discovered_devices = []
    discovered_devices = await scan_ble_devices()
​
    def is_compatible(dname):
        #compatibles = [ "HX06", "MI Band 2", "Amazfit Band 2", "Mi Smart Band 4", "C11(ID-A54E)", "Galaxy Fitⓔ (4B78)", "SWR10", "" ]
        compatibles = [ "MI Band 2" ]
        for cname in compatibles:
            if dname == cname:
                return True
        return False
    
    for device in discovered_devices:
        if is_compatible(device.name):
            async with BleakClient(device.address, loop=loop) as client:
                x = await client.is_connected()
                log.info("Connected: {0}".format(x))
                
                scanning = False
                if scanning == True:
                    for service in client.services:
                        log.info("[Service] {0}: {1}".format(service.uuid, service.description))
                        for char in service.characteristics:
                            try:
                                value = bytes(await client.read_gatt_char(char.uuid))
                                log.info("\t[Characteristic] {0}: (Handle: {1}) ({2}) | Name: {3}, Value: {4} ".format(char.uuid, char.handle, ",".join(char.properties), char.description, value))
                            except Exception as e:
                                value = str(e).encode()
                                log.info("\t[Characteristic] {0}: (Handle: {1}) ({2}) | Name: {3}, ExceptionValue: {4} ".format(char.uuid, char.handle, ",".join(char.properties), char.description, value))
                            else:
                                value = None  
                
                try:
                    value = bytes(await client.read_gatt_char(73))
                    log.info("Steps value: {0} ".format( value))
                    steps = int.from_bytes(value[1:2], byteorder='big', signed=False)
                    log.info("Steps int value: {0}".format(steps))
                except Exception as e:
                    value = str(e).encode()
                    log.info("Exception occurred {0}".format(e))
                
                try:
                    value = bytes(await client.read_gatt_char(43))
                    log.info("Heart Rate value: {0} ".format(value))
                except Exception as e:
                    value = str(e).encode()
                    log.info("Exception occurred {0}".format(e))
                
                def steps_handler(sender,data):
                    log.info("Steps >>> {0}: {1}".format(sender, data))
                    
                def heartrate_handler(sender,data):
                    log.info("Heart Rate >>> {0}: {1}".format(sender, data))
​
                def auth_handler(sender, data):
                    global newdata
                    newdata = data
                    
                # some random encryption keys
                #fixed_miband2_key = b'\x30\x31\x32\x33\x34\x35\x36\x37\x38\x39\x40\x41\x42\x43\x44\x45'
                #fixed_miband2_key = b'\xf5\xd2\x29\x87\x65\x0a\x1d\x82\x05\xab\x82\xbe\xb9\x38\x59\xcf'
                fixed_miband2_key = b'\xa8\xd5\x1e\xf1\x24\x9d\xa1\xb3\x6c\xed\xf9\x9e\x72\x6a\xc8\x7a'
                
                ############################
                # AUTHENTICATION
                ############################
                
                # 1) sending 16 bytes encryption key with prefix \x01\x00
                log.info("Sending custom encryption key") # Mi Band 2 vibrates, asking to confirm authentication
                # whenever you press the button, you reset the values of the MI Band 2 and permanently set the encryption key
                # after the first time the encryption key is set, there is no need to send this packet against (unless you want to change it or reset values)
                await client.write_gatt_char(83,bytearray(b'\x01\x00')+bytearray(fixed_miband2_key),True)
                await asyncio.sleep(5)
                
                
                ############################
                # READING PROTECTED CHARACTERISTICS
                ############################
                
                # 1) setting on auth notifications
                log.info("Enabling auth notification")
                await client.start_notify(83, auth_handler)
                await asyncio.sleep(1)
                # 2) requesting random challenge sending \x02\x00
                log.info("Sending \x02\x00")
                await client.write_gatt_char(83,bytearray(b'\x02\x00'),True)
                # 3) await response with random challenge
                await asyncio.sleep(5)
                #log.info(newdata)
                if newdata[:3] == b'\x10\x02\x01':
                    key = newdata[-16:]
                    #log.info("Received key {0}".format(key))
                    # 4) encrypting with our key and send back with prefix \x03\x00
                    cipher = AES.new(fixed_miband2_key, AES.MODE_ECB)
                    encrypt_msg = cipher.encrypt(bytes(key))
                    prefix_encrypt_msg = bytearray(b'\x03\x00') + bytearray(encrypt_msg)
                    log.info("Sending final encrypted message")
                    await client.write_gatt_char(83,bytearray(prefix_encrypt_msg),True)
                    # 5) notification off
                    await asyncio.sleep(5)
                    log.info("Stopping auth notification")
                    await client.stop_notify(83)
                    
                    #read steps
                    try:
                        value = bytes(await client.read_gatt_char(73))
                        log.info("Steps value: {0} ".format(value))
                        steps = int.from_bytes(value[1:2], byteorder='big', signed=False)
                        log.info("Steps int value: {0}".format(steps))
                    except Exception as e:
                        value = str(e).encode()
                        log.info("Exception occurred {0}".format(e))
                    
                    # test notifications about steps and heart rate
                    notification_test = True
                    if notification_test == True:
                        await client.start_notify(73, steps_handler)
                        await asyncio.sleep(20)
                        await client.stop_notify(73)   
                         
                        await client.start_notify(40, heartrate_handler)
                        await asyncio.sleep(20)
                        await client.stop_notify(40)
        
​
loop = asyncio.get_event_loop()
loop.run_until_complete(run(loop, True))
loop.close()
