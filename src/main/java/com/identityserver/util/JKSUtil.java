package com.identityserver.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.spec.RSAPublicKeySpec;

/**
 * Created by mcg on 2019/8/13.
 */
public class JKSUtil {

    private String keyStoreFile;
    private char[] password;
    private KeyStore store;
    private Object lock = new Object();

    private static JKSUtil instance = null;

    public static JKSUtil getInstance() {
        synchronized (JKSUtil.class) {
            if (instance == null) {
                synchronized (JKSUtil.class) {
                    instance = new JKSUtil("/keystore.jks", "foobar".toCharArray());
                }
            }
            return instance;
        }
    }

    private JKSUtil(String _jksFilePath, char[] password) {
        this.keyStoreFile = _jksFilePath;
        this.password = password;
    }

    public KeyPair getKeyPair(String alias) {
        return getKeyPair(alias, this.password);
    }

    public KeyPair getKeyPair(String alias, char[] password) {
        try {
            synchronized (this.lock) {
                if (this.store == null) {
                    synchronized (this.lock) {
                        InputStream is = this.getClass().getResourceAsStream(keyStoreFile);
                        try {
                            this.store = KeyStore.getInstance("JKS");
                            this.store.load(is, this.password);
                        } finally {
                            if (is != null) {
                                try {
                                    is.close();
                                } catch (Exception e) {
                                }
                            }
                        }
                    }
                }
            }
            RSAPrivateCrtKey key = (RSAPrivateCrtKey) this.store.getKey(alias, password);
            RSAPublicKeySpec spec = new RSAPublicKeySpec(key.getModulus(), key.getPublicExponent());
            PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(spec);
            return new KeyPair(publicKey, key);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot load keys from store: " + this.keyStoreFile, e);
        }
    }

    public static String generateAccessToken() throws Exception {
        KeyPair keyPair = JKSUtil.getInstance().getKeyPair("test");
        String compactJws = Jwts.builder().setSubject("MyService")
                .signWith(SignatureAlgorithm.RS256, keyPair.getPrivate()).compact();
        System.out.println(compactJws);
        return compactJws;
    }
}
