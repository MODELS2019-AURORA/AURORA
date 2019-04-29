import os
import re
import string
import textmining
import nltk
from nltk.stem.porter import PorterStemmer
from nltk.stem import WordNetLemmatizer
from nltk.corpus import stopwords
from nltk.tokenize import word_tokenize

#Init
rootOutput = 'TDMsMM_BIGRAMS2/'
TEMP_MATRIX_FILE = rootOutput + 'matrixMM.csv'
RESULT_MATRIX_FILE = rootOutput + 'matrixLabeled.csv'
MAP_FILE = rootOutput + "map.txt"
INPUT_PATH = '/home/juri/PycharmProjects/mergeDB/demo/'
nltk.download('punkt')
nltk.download('stopwords')
set(stopwords.words('english'))
printable = set(string.printable)

def no_tokenizer(document):
    document = document.lower()
    return document.strip().split()


def myIR(text):
    text = text.lower()
    splits = text.split(" ")
    result = ''
    for s in splits:
        if(len(s.split(".")) == 2):
            p1,p2 = s.split(".")
            line = ir_text(p1).strip() + "." + ir_text(p2).strip()
            result = result + " " + line
    return result

def termdocumentmatrix(input_path = INPUT_PATH, cutoff = 1, ir = True):
    listFile = walking_dir(input_path)
    count = 1
    reverseMap = {}
    tdm = textmining.TermDocumentMatrix(tokenizer=no_tokenizer)
    for file in listFile:
        text = ''
        with open(file, 'r') as myfile:
            for curline in myfile:
                text = text + curline
        text = filter(lambda x: x in printable, text)
        reverseMap[count] = file
        count = count + 1
        if ir: tdm.add_doc(ir_text(text))
        else: tdm.add_doc(myIR(text))

    tdm.write_csv(TEMP_MATRIX_FILE+ str(cutoff), cutoff=cutoff)

    max = 0;
    with open(TEMP_MATRIX_FILE + str(cutoff), 'r') as matrix:
        for cnt, line in enumerate(matrix):
            if cnt != 0:
                part = line.split(",")
                for value in part:
                    if max < value:
                        max = value

    with open(TEMP_MATRIX_FILE + str(cutoff), 'r') as matrix:
        with open (RESULT_MATRIX_FILE + str(cutoff), "w") as matrixLabeled:
            for cnt, line in enumerate(matrix):
                if cnt != 0:
                    fileName = reverseMap[cnt].replace(INPUT_PATH,"")[4:7]
                    normalizedRow = normalize(line.rstrip(),max)
                    matrixLabeled.write(normalizedRow + "," + fileName + "\n")


    with open (MAP_FILE + str(cutoff), "w") as writer:
        for key, value in reverseMap.items():
            writer.write(str(key) + "\t" + str(value) + "\n")

def normalize(string, maxValue):
    result = ''
    parts = string.split(",")
    for part in parts:
        result = result + str(float(part)/float(maxValue)) + ","
    result = result[:-1]
    return result

def stoppingwords(string):
    stop_words = set(stopwords.words('english'))

    word_tokens = word_tokenize(string)
    filtered_sentence = ''
    for w in word_tokens:
        if w not in stop_words:
            filtered_sentence =  filtered_sentence + ' ' + w
    return filtered_sentence


def stemming(string):
    porter_stemmer = PorterStemmer()
    word_data = string
    nltk_tokens = nltk.word_tokenize(word_data)
    result = ''
    for w in nltk_tokens:
        result = result + ' ' + porter_stemmer.stem(w)
    return result

def lemmatization(string):
    wordnet_lemmatizer = WordNetLemmatizer()
    word_data = string
    nltk_tokens = nltk.word_tokenize(word_data)
    result = ''
    for w in nltk_tokens:
        result = result + ' ' + wordnet_lemmatizer.lemmatize(w)
    return result

def ir_text(string): return stoppingwords(lemmatization(stemming(string)))

def walking_dir(rootdir):
    fileList = []
    for root, subdirs, files in os.walk(rootdir):
        for name in files:
            fileList.append(os.path.join(root,name))
    return fileList
for i in range(1,4):
    print 'iteration ' + str(i)
    termdocumentmatrix(INPUT_PATH, i, ir=False)