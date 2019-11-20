
import matplotlib.pyplot as plt
import csv

filePath = "../../results.txt"
readWriteMode = "r"
xValues = []
yValues = []

with open (filePath, readWriteMode) as f:
    for row in csv.reader(f,delimiter=' '):
        xValues.append(row[0])
        yValues.append(row[3])
        if(row[0]>8 and row[0<11]):
            print(row[6])
        #print(row[3])

time = [0, 1, 2, 3]
position = [0, 100, 200, 300]

#plt.plot(time, position)
plt.plot(xValues, yValues)
plt.xlabel('Time (hr)')
plt.ylabel('Position (km)')
plt.show()
#df = pd.read_csv(file_path, header=None, usecols=[3,6])
