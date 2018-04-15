/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lib;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 *
 * @author Hoang
 */
public class CryptoData {
    public KeyPair GenkeyPair() throws NoSuchAlgorithmException {
        final int keysize = 512;
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(keysize);
        return keyGen.genKeyPair();
    }

    public byte[] encryptText(PublicKey publicKey, String message) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(message.getBytes());
    }

    public byte[] decryptText(PrivateKey privateKey, byte[] encrypt) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(encrypt);
    }

    public void encryptFile(PublicKey publicKey, String fileName) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(new File(fileName));
            outputStream = new FileOutputStream(new File(fileName + ".enc"));
            byte[] inputBuff = new byte[512];
            int data;
            while ((data = inputStream.read(inputBuff)) != -1) {
                byte[] outputBuff = cipher.update(inputBuff, 0, data);
                if(outputBuff != null) {
                    outputStream.write(outputBuff);
                }
            }
            byte[] outputBuff = cipher.doFinal();
            if(outputBuff != null) {
                outputStream.write(outputBuff);
            }
            inputStream.close();
            outputStream.close();
        } catch (FileNotFoundException ex) {

        } catch (IOException ex) {
            
        }
    }

    public void decryptFile(PrivateKey privateKey, byte[] encrypt) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

    }
}
