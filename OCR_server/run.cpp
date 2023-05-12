#include <iostream>

#include <FJML.h>

#include "OCR_server_AthleteOCR.h"

JNIEXPORT jint JNICALL Java_OCR_1server_AthleteOCR_getAthleteNumber(JNIEnv* env, jclass obj, jdoubleArray img) {
    std::cout << "Hello world" << std::endl;
    jdouble* img_arr = env->GetDoubleArrayElements(img, 0);
    FJML::Tensor image({1, 784});
    for (int i = 0; i < 784; i++) {
        image.data[i] = img_arr[i];
        std::cout << i << " " << image.data[i] << std::endl;
    }
    FJML::MLP model;
    model.load("OCR_server/mnist.fjml");
    model.summary();
    FJML::Tensor result = model.run(image);
    return static_cast<jint>(FJML::LinAlg::argmax(result));
}
