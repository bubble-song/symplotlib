import matplotlib.pyplot as plt
import numpy as np

x = np.linspace(-1, 1, 100000)
print(x)
y = 2*x**2

plt.plot(x, y, "r*")
plt.ylabel("haha")
plt.grid()
plt.show()
