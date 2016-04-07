#!/usr/bin/env python
# coding: utf-8

import matplotlib.pyplot as plt
import numpy as np

colors = ["red", "blue", "brown", "orange", "teal"]

f = open("output1.txt")

Clusters = ["Cluster "+str(i) for i in range(0,5)]
arr_of_xs = []
arr_of_ys = []
clusterNum = -1;
for line in f:
    if line[0] != "C":
        comps = line.split(", ");
        arr_of_xs[clusterNum].append(float(comps[0]))
        arr_of_ys[clusterNum].append(float(comps[1]))
    else:
        arr_of_xs.append([])
        arr_of_ys.append([])
        clusterNum += 1

for i in range(0, len(arr_of_xs)):
    xs = arr_of_xs[i]
    ys = arr_of_ys[i]
    plt.scatter(xs, ys, s=50, c=colors[i])
plt.show()