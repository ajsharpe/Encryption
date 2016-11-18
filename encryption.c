#include <stdio.h>
#include <jni.h>
#include <stdlib.h>
#include "Encryption.h"

/*----------------------ENCRYPT----------------------*/
JNIEXPORT jintArray JNICALL Java_Encryption_encrypt
(JNIEnv *env, jobject object, jintArray v, jintArray key){
/*void encrypt (int *v, int *k){
/* TEA encryption algorithm */
    int delta = 0x9e3779b9;
    unsigned int y = 0, z = 0, n=1, sum = 0;
    jboolean *is_copy;

    /* obtain data to encrypt */
    jsize length = (*env)->GetArrayLength(env, v);
    jint *buf = (jint *) (*env)->GetIntArrayElements(env, v, is_copy);
    if (buf == NULL || length > 2){
        printf("Cannot obtain data from JVM\n");
        exit(0);
    }

    /* obtain key */
    int *k = (int*) (jint*) (*env)->GetIntArrayElements(env, key, is_copy);
    length = (*env)->GetArrayLength(env, key);
    if (k == NULL || length != 4){
        printf("Cannot obtain key from JVM\n");
        exit(0);
    }

    /* encrypt */    
    y= (unsigned int) buf[0];
    z= (unsigned int) buf[1];	
	while (n-- > 0){
		sum += delta;
		y += ((z<<4) + k[0]) ^ (z + sum) ^ ((z>>5) + k[1]);
		z += ((y<<4) + k[2]) ^ (y + sum) ^ ((y>>5) + k[3]);
	}

    jintArray result = (*env)->NewIntArray(env, (jsize) 2);
    jint encrypted[2] = {(jint)y, (jint)z};
    (*env)->SetIntArrayRegion(env, result, 0, (jsize) 2, encrypted);

    return result;
}

/*----------------------DECRYPT----------------------*/
JNIEXPORT jintArray JNICALL Java_Encryption_decrypt
(JNIEnv *env, jobject object, jintArray v, jintArray key){
/*void decrypt (int *v, int *k){
/* TEA decryption algorithm */
    int delta = 0x9e3779b9;
    unsigned int y = 0, z = 0, n=1, sum = delta;//0xC6EF3720;//delta<<5;
    jboolean *is_copy;

    /* obtain data to decrypt */
    jsize length = (*env)->GetArrayLength(env, v);
    jint *buf = (jint *) (*env)->GetIntArrayElements(env, v, is_copy);
    if (buf == NULL || length > 2){
        printf("Cannot obtain data from JVM\n");
        exit(0);
    }

    /* obtain key */
    int *k = (int*) (jint*) (*env)->GetIntArrayElements(env, key, is_copy);
    length = (*env)->GetArrayLength(env, key);
    if (k == NULL || length != 4){
        printf("Cannot obtain key from JVM\n");
        exit(0);
    }

    /* decrypt */   
    y= (unsigned int) buf[0];
    z= (unsigned int) buf[1];	
	while (n-- > 0){
		z -= ((y<<4) + k[2]) ^ (y + sum) ^ ((y>>5) + k[3]);
		y -= ((z<<4) + k[0]) ^ (z + sum) ^ ((z>>5) + k[1]);
		sum -= delta;
	}	
    jintArray result = (*env)->NewIntArray(env, (jsize) 2);
    jint decrypted[2] = {(jint)y, (jint)z};
    (*env)->SetIntArrayRegion(env, result, 0, (jsize) 2, decrypted);

    return result;
}

