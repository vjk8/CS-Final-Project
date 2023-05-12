// The following code was taken from the FJML repository
//
// Copyright (c) 2023 David Lee
// This code is licensed under MIT license (see LICENSE for details)

#include <chrono>
#include <fstream>
#include <iostream>
#include <sstream>
#include <string>
#include <vector>

#include <FJML.h>

/**
 * @brief Load data from a csv file
 * @param x The input data
 * @param y The output data
 * @param filename The name of the file to load
 */
void load_data(FJML::Tensor& x, FJML::Tensor& y, std::string filename, int limit = -1) {
    // Uses data from the kaggle mnist dataset
    // https://www.kaggle.com/datasets/oddrationale/mnist-in-csv
    std::ifstream file(filename);

    // The first line of the file is the header
    std::string line;
    std::getline(file, line);

    // Read the data
    std::vector<FJML::Tensor> x_vec, y_vec;
    while (std::getline(file, line)) {
        std::stringstream ss(line);

        // The first value is the label
        int label;
        ss >> label;

        // The rest of the values are the pixels
        FJML::Tensor pixels({28 * 28});
        for (int i = 0; i < 28 * 28; i++) {
            int pixel;
            char comma;
            ss >> comma >> pixel;
            pixels.at(i) = pixel / 255.0;
        }

        // Add the data to the vectors
        x_vec.push_back(pixels);
        y_vec.push_back(FJML::Data::one_hot(label, 10));

        // Stop if we have enough data
        if (limit != -1 && (int)x_vec.size() >= limit) {
            break;
        }
    }

    // Convert the vectors to tensors
    x = FJML::Tensor::array(x_vec);
    y = FJML::Tensor::array(y_vec);
}

int main() {
    // Load the data
    FJML::Tensor mnist_train_x, mnist_train_y;
    FJML::Tensor mnist_test_x, mnist_test_y;
    std::chrono::steady_clock::time_point begin = std::chrono::steady_clock::now();
    load_data(mnist_train_x, mnist_train_y, "mnist_train.csv");
    load_data(mnist_test_x, mnist_test_y, "mnist_test.csv");
    std::cout << "Loaded " << mnist_train_x.shape[0] << " training samples and " << mnist_test_x.shape[0]
              << " testing samples" << std::endl;
    std::chrono::steady_clock::time_point end = std::chrono::steady_clock::now();
    std::cout << "Loading took " << std::chrono::duration_cast<std::chrono::milliseconds>(end - begin).count() / 1000.0
              << " seconds" << std::endl;

    // Split the data into training and validation sets
    FJML::Tensor x_train, y_train;
    FJML::Tensor x_test, y_test;
    FJML::Data::split(mnist_train_x, mnist_train_y, x_train, y_train, x_test, y_test, 0.8);

    // Create the model
    // The model is a simple MLP with 1 hidden layer
    // The input layer has 28 * 28 = 784 neurons
    // The hidden layer has 128 neurons
    // The output layer has 10 neurons
    //
    // The model constructor takes 3 arguments:
    // 1. A vector of layers
    // 2. A loss function
    // 3. An optimizer
    // Change the device to FJML::DEVICE_CUDA to use the GPU
    FJML::MLP model({new FJML::Layers::Dense(28 * 28, 128, FJML::Activations::relu, FJML::DEVICE_CPU),
                     new FJML::Layers::Dense(128, 10, FJML::Activations::linear, FJML::DEVICE_CPU),
                     new FJML::Layers::Softmax()},
                    FJML::Loss::crossentropy, new FJML::Optimizers::Adam());

    // Train the model
    model.train(x_train, y_train, x_test, y_test, 6, 128, "mnist.fjml");

    // Evaluate the model
    std::cout << "Training accuracy: " << model.calc_accuracy(x_train, y_train) << std::endl;
    std::cout << "Testing accuracy: " << model.calc_accuracy(mnist_test_x, mnist_test_y) << std::endl;
}

// Compile with:
// g++ -O3 -o main main.cpp -lFJML
