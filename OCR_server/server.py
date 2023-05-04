import fastapi
import PIL
import PIL.Image
import pytesseract

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
    text = pytesseract.image_to_string(pil_image)
    return {'text': str(text)}
