#!/usr/bin/env python3

"""
Bozdoğan CObject implementation for Disguiser project
"""

import hashlib
from base64 import b64encode, b64decode
from Crypto.Cipher import AES
from Crypto.Util.Padding import pad, unpad


def apply_basic_padding(text, blocksize=16):
    """just fill with null bytes at the end"""
    _p = len(text)%blocksize # padding length
    return text + (_p > 0 and bytes(blocksize - _p) or b'\0')
    # =
    # return text + (bytes(blocksize - _p) if _p > 0 else b'')

# Bozdogan Bilisim
class CObject(object):
    """CObject version 1.0 implementation
    Designed to be cross-compatible with Java version of API."""
    def __init__(self, passphrase=None):
        self.ENCODING = "utf-8"
        self.key = None
        if passphrase: self.set_password(passphrase)

    def set_password(self, passphrase):
        """sets the master password"""
        self.key = hashlib.new("md5", passphrase.encode(self.ENCODING)).digest()
        self.cipher = AES.new(self.key, AES.MODE_ECB)

    def encrypt(self, plaintext):
        """encrypts a message. a password must be set beforehand.
        note that some padding may be appended in order to reach the block size."""
        if not self.key: return -1
        try:
            if type(plaintext) == str:
                plaintext = plaintext.encode(self.ENCODING)

            return b64encode(self.cipher.encrypt(pad(plaintext, 16, "pkcs7")))
        except ValueError:
            return -1 #@!

    def decrypt(self, ciphertext):
        """decrypts a message. a password must be set beforehand
        any padding will be trimmed."""
        if not self.key: return -1
        try:
            return unpad(self.cipher.decrypt(b64decode(ciphertext)), 16, "pkcs7").decode(self.ENCODING)
        except ValueError:
            return -1 #@!


if __name__ == '__main__':
    rawdata = "sonra beraber olsak diyoruz. en ufak tebessümden yüz buluyoruz\n" \
              "her yere kiraz ağaçlarından pembe karlar yağdırıp \"beni affet\" diye yalvarıyoruz\n" \
              "sonra yine yine aldatıyoruz."

    cobj = CObject("1234")
    print(cobj.encrypt(rawdata))
    print(cobj.decrypt("+3cp5joWLA9Ez+UaMZHKsMCBwUcE+9NlaIPvgMWi4jhXVISybXTY184UVmfONfyvQKccdbcZE6VC+MENS8YcZnCHZD96Rwqr2ZUGWMXiK1TQYqpvIg4PXti7bbkqqFrgKCibNI5o6EEuooP/6jMx4cua2gAVuK7UoXZFG7ZFmrPdEJN85jllf2+y373US+lYmEeV7zor56Sg3I+KmXa6Dhqt+4ksw6CLwRVmxI+JKN2lJqomAOKodakFo1/NffO2"))

    print()

    cobj2 = CObject()

    print(cobj2.encrypt("bir şeyler")) # trying to encrypt sth without setting a password
    cobj2.set_password("1234456seven")
    print(cobj2.decrypt(b'uJFPjR+pqwn7ys8pyIPJog=='))
    print(cobj2.decrypt(b'gvIy/89sbL4QslgDcd4d1Q==')) # trying to decrypt the text encrypted w/ different pw
    print(cobj2.encrypt("Angela"))

    print("\n", cobj.key, "\n",cobj2.key)
    print("\n", cobj.key.hex(), "\n", cobj2.key.hex())