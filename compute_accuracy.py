#!/usr/bin/python

import sys
import os


if len(sys.argv) != 2: 
    print 'error ** usage: %s data predictions' % sys.argv[0]
    print 'error ** please try: majority|even_odd|logistic_regression'
    sys.exit()

algorithm = sys.argv[1]

match = 0

if algorithm == 'majority':
	match = 1
elif algorithm == 'even_odd':
	match = 1
elif algorithm == 'logistic_regression':
	match = 1

if match == 0:
	print 'error ** no matching for algorithm: %s' % sys.argv[1]
	print 'error ** please try: majority|even_odd|logistic_regression'
	sys.exit()

print 'classify algorithm: ' + algorithm

dataList = ['vision/vision','nlp/nlp','speech/speech','finance/finance','bio/bio','synthetic/easy','synthetic/hard']
dataName = ['vision','nlp','speech','finance','bio','easy','hard']

for data in dataList:
	print '<'+dataName[dataList.index(data)]+'>'

	cmd = 'java -cp ../lib/commons-cli-1.3.1.jar: cs475/Classify -mode train -algorithm ' + algorithm +' -model_file ../output/'+data+'.'+algorithm+'.model -data ../data/'+data+'.train'
	os.system(cmd)

	cmd = 'java -cp ../lib/commons-cli-1.3.1.jar: cs475/Classify -mode test -model_file ../output/'+data+'.'+algorithm+'.model -data ../data/'+data+'.dev -predictions_file ../output/'+data+'.dev.predictions'
	os.system(cmd)

#print 'Accuracy: %f (%d/%d)' % ((float(match)/float(total)), match, total)
