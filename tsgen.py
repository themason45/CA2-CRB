"""
This simply just generates the indices for the time slots for the CSV file
"""

import random

min = 0
max = 20

list = []
for i in range(0, random.randint(4, 6)):
    list.append(str(random.randint(min, max)))

print('[{}]'.format(" ".join(list)))

