import numpy as np
import matplotlib.pyplot as plt

temp_up = 150
temp_down = 200
temp_left = 100
temp_right = 50

n = 41
length = 1
width = 1

dx = length / (n - 1)
dy = width / (n - 1)

x = np.linspace(0, length, n)
y = np.linspace(0, width, n)
X, Y = np.meshgrid(x, y)

a = 1 / dx ** 2
b = 1 / dy ** 2
c = -2 * (a + b)

A = np.zeros((n ** 2, n ** 2))
B = np.zeros(n ** 2)

for i in range(n):
    for j in range(n):
        k = i * n + j


        if i == 0:
            A[k, k] = 1
            B[k] = temp_up
        elif i == n - 1:
            A[k, k] = 1
            B[k] = temp_down
        elif j == 0:
            A[k, k] = 1
            B[k] = temp_left
        elif j == n - 1:
            A[k, k] = 1
            B[k] = temp_right

        else:
            A[k, k] = c
            A[k, k - 1] = a
            A[k, k + 1] = a
            A[k, k - n] = b
            A[k, k + n] = b


T = np.linalg.solve(A, B)


T = T.reshape((n, n))


plt.contourf(X, Y, T)
plt.colorbar()
plt.show()
