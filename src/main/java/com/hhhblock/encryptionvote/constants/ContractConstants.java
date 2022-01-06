package com.hhhblock.encryptionvote.constants;

import java.lang.Exception;
import java.lang.RuntimeException;
import java.lang.String;

public class ContractConstants {
    public static String PaillierPrecompiledAbi;

    public static String PaillierPrecompiledBinary;

    public static String PaillierPrecompiledGmBinary;

    public static String CallPaillierAbi;

    public static String CallPaillierBinary;

    public static String CallPaillierGmBinary;

    public static String HelloWorldAbi;

    public static String HelloWorldBinary;

    public static String HelloWorldGmBinary;

    static {
        try {
            PaillierPrecompiledAbi = org.apache.commons.io.IOUtils
                    .toString(
                            Thread.currentThread().getContextClassLoader().getResource("abi/PaillierPrecompiled.abi"));
            PaillierPrecompiledBinary = org.apache.commons.io.IOUtils
                    .toString(Thread.currentThread().getContextClassLoader()
                            .getResource("bin/ecc/PaillierPrecompiled.bin"));
            PaillierPrecompiledGmBinary = org.apache.commons.io.IOUtils
                    .toString(Thread.currentThread().getContextClassLoader()
                            .getResource("bin/sm/PaillierPrecompiled.bin"));
            CallPaillierAbi = org.apache.commons.io.IOUtils
                    .toString(Thread.currentThread().getContextClassLoader().getResource("abi/CallPaillier.abi"));
            CallPaillierBinary = org.apache.commons.io.IOUtils
                    .toString(Thread.currentThread().getContextClassLoader().getResource("bin/ecc/CallPaillier.bin"));
            CallPaillierGmBinary = org.apache.commons.io.IOUtils
                    .toString(Thread.currentThread().getContextClassLoader().getResource("bin/sm/CallPaillier.bin"));
            HelloWorldAbi = org.apache.commons.io.IOUtils
                    .toString(Thread.currentThread().getContextClassLoader().getResource("abi/HelloWorld.abi"));
            HelloWorldBinary = org.apache.commons.io.IOUtils
                    .toString(Thread.currentThread().getContextClassLoader().getResource("bin/ecc/HelloWorld.bin"));
            HelloWorldGmBinary = org.apache.commons.io.IOUtils
                    .toString(Thread.currentThread().getContextClassLoader().getResource("bin/sm/HelloWorld.bin"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
