import pandas as pd
import numpy as np
import os
import socket
import win32com.client
import pickle
import statistics
# from fastdtw import fastdtw
# from sklearn.base import BaseEstimator, ClassifierMixin
from ModifiedDTW import KnnDtwClassifier
#root_path
dir=os.path.dirname(os.path.realpath('__file__'))
test_data_raw = []
#training label sets
gesture_dict={'left':os.path.join(dir, 'ANDROID','gestures','left'),
         "right":os.path.join(dir, 'ANDROID','gestures','right'),
        "top":os.path.join(dir, 'ANDROID','gestures','top'),
        "bottom":os.path.join(dir, 'ANDROID','gestures','bottom')}
        # opening pickle
def open_pickle():
    with open('DTW_model','rb') as f:
        clf3=pickle.load(f)
    with open('train_labels','rb') as f:
        TL=pickle.load(f)
        return [clf3,TL]
##------------------------------------##
##----------ONE TIME RUN--------------##
temparray=open_pickle()
clf3=temparray[0]
train_labels=temparray[1]
print("# import successful #")
##------------------------------------##
#normalizing the df
def normalize(v):
    norm = np.linalg.norm(v)
    if norm == 0:
        return v
    return v / norm
#here all predicting things are done
def predict(data_predict):
    print("in predict\n",data_predict)
    arr=list()
    for i in data_predict:
        x=list(map(float,i.split()))
        arr.append(x)
    df=pd.DataFrame(arr)
    print(df)
    print("no error")

    td = normalize(np.ravel(df)) #NORMALIZING
    res = clf3.predict_ext(td) #PREDICTING
    nghs = np.array(train_labels)[res[1]] #FINDING THE TRIPLETS

    #chossing majority of 3
    d=dict()
    max=-1
    val=""
    for i in nghs:
        if i in d:
            d[i]+=1
        else:
            d[i]=1
        if d[i]>max:
            max=d[i]
            val=i
    print("KnnDtwClassifier neighbors for " + " = " + str(nghs))
    print("KnnDtwClassifier distances to " + str(nghs) + " = " + str(res[0]))
    print("choosing =======>",val)

    if val=='top':
        presentation.SlideShowWindow.View.Next()
    elif val=='bottom':
        presentation.SlideShowWindow.View.Previous()
    else:
        print("$$$$$$$$$ exiting $$$$$$$$$")
        # presentation.SlideShowWindow.View.Exit()
        # app.Quit()
        presentation.SlideShowWindow.View.Next()


def CaptureData(gesture,datas):
    dirname=gesture_dict[gesture]
    l = len(os.listdir(dirname))
    with open(dirname+"\\"+str(l)+".txt",'a') as f: #loading most recent model
        for data in datas:
            f.write(data+"\n")
    print("gesture",gesture,"\ndatas",datas)
    
    
PORT = 7800
app = win32com.client.Dispatch("PowerPoint.Application")
presentation = app.Presentations.Open(FileName=u'C:\\Users\\PRANJUL\\Desktop\\FYP\\controlling_ppt\\test.pptx', ReadOnly=1)
presentation.SlideShowSettings.Run()

while(True):
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s: #creating socket with ipv4 and port
        s.bind((socket.gethostname(), PORT))
        s.listen(5)
        conn, addr = s.accept() #conn having data and addr having ip
        with conn:
            print('Connected by', addr)
            while True:
                datatemp = conn.recv(1024) #recieving data
                data=datatemp.decode("utf-8") #b to charater utf-8 encoding
                finaldata=data.split('\n')
                if not datatemp:
                    break
                elif finaldata=="forward":
                    # app = win32com.client.Dispatch("PowerPoint.Application")
                    # presentation = app.Presentations.Open(FileName=u'C:\\Users\\PRANJUL\\Desktop\\FYP\\controlling_ppt\\test.pptx', ReadOnly=1)
                    # presentation.SlideShowSettings.Run()
                    # presentation.SlideShowWindow.View.Next()
                    print("commented hai, hatane se pehle global lines ko upar le jaana mat bhoolna")
                elif finaldata=="backward":
                    # app = win32com.client.Dispatch("PowerPoint.Application")
                    # presentation = app.Presentations.Open(FileName=u'C:\\Users\\PRANJUL\\Desktop\\FYP\\controlling_ppt\\test.pptx', ReadOnly=1)
                    # presentation.SlideShowSettings.Run()
                    # presentation.SlideShowWindow.View.Previous()
                    print("commented hai, hatane se pehle global lines ko upar le jaana mat bhoolna")
                elif finaldata[0]=="Predicting":
                    predict(finaldata[1:-2])
                elif finaldata[0]=="CaptureData":
                    print("here")
                    CaptureData(finaldata[1],finaldata[2:-1])
#                 elif finaldata[0]=="train":
# #                     trainmodel()
                else:
                    print("something gadbad aaya")
                    
                    
                                            