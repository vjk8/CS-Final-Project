import os

import requests

if __name__ == '__main__':
    for filename in os.listdir('data'):
        result = requests.post('http://127.0.0.1:6969/run', files={
                               'image': open(f'data/{filename}', 'rb')})
        print(filename, result, result.json())
