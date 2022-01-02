#!/usr/bin/env python3
import sys
import os

#_TEMPFILE_PREFIX  = "~tmp."
#_TEMPFILE_POSTFIX = ""
_ENCRYPTED_DIR = "toylanan"

# Bozdogan Bilisim A.S.
if __name__ == '__main__':
    if len(sys.argv) < 3: print("! HATALI GİRİŞ."); quit(1)
    dosyaKonumu, etkenFaktor = sys.argv[1:3]

    try:
        etkenFaktor = int(etkenFaktor) % 256
    except ValueError:
        etkenFaktor = ord(etkenFaktor[0]) % 256

    os.system("mkdir {}".format(_ENCRYPTED_DIR))
    os.system("del {}".format(_ENCRYPTED_DIR+"/"+dosyaKonumu))

    with open(dosyaKonumu,'rb') as dIlk, open(_ENCRYPTED_DIR+"/"+dosyaKonumu,'wb') as dGec:
        while True:
            karakter = dIlk.read(1)
            if not karakter: break
            dGec.write(bytes(chr(ord(karakter) ^ etkenFaktor),"iso8859-1"))

    #print(dosyaKonumu,etkenFaktor)
    print(". MUHTEMELEN BAŞARDIK")
	
# toylandin01 SONU
