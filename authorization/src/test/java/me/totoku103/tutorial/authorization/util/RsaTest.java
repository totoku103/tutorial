package me.totoku103.tutorial.authorization.util;

import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;

class RsaTest {

    @Test
    public void test1() throws NoSuchAlgorithmException {
        Rsa rsa = new Rsa();

        KeyPair keypair = rsa.createKeypair();
    }

}