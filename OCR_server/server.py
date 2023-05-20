import fastapi
import PIL
import PIL.Image
import PIL.ImageEnhance
import pytesseract
from pytesseract import Output

api = fastapi.FastAPI()


@api.get('/')
async def ping():
    '''
    Ping method to check if the API is online
    '''
    return {'message': 'Hello World'}


@api.post('/run')
async def run_ocr(image: fastapi.UploadFile):
    '''
    Run the ocr with the given image
    '''
    pil_image = PIL.Image.open(image.file)
    sharpen = PIL.ImageEnhance.Sharpness(pil_image).enhance(1.3)
    recolor = PIL.ImageEnhance.Color(sharpen).enhance(0.0)
    bright = PIL.ImageEnhance.Brightness(recolor).enhance(2.0)
    contrast = PIL.ImageEnhance.Contrast(bright).enhance(2.0)
    contrast.save(f'test_{image.filename}')
    data = pytesseract.image_to_data(
        contrast, config='-c tessedit_char_whitelist=0123456789 --psm 10 ', output_type=Output.DICT)
    print(image.filename, data)
    best = 0
    for level in range(len(data['level'])):
        if data['conf'][level] > data['conf'][best]:
            best = level
    try:
        return {'number': int(data['text'][best].strip()), 'confidence': data['conf'][level]}
    except:
        return {'number': -1, 'confidence': -1}
