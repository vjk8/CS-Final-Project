#include <iostream>

#include <FJML.h>

int main() {
    std::cout << "Hello world" << std::endl;
    FJML::Tensor image({1, 784});
    FJML::MLP model;
    model.load("OCR_server/mnist.fjml");
    model.summary();
    FJML::Tensor result = model.run(image);
    std::cout << result << " " << FJML::LinAlg::argmax(result) << std::endl;
}
