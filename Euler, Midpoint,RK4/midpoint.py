import numpy as np
import matplotlib.pyplot as plt

t0, tf, dt = 0, 15, 0.001
A, B, C = 10, 25, 8/3
x0, y0, z0 = 1, 1, 1

t = np.arange(t0, tf+dt, dt)

x, y, z = np.zeros_like(t), np.zeros_like(t), np.zeros_like(t)

x[0], y[0], z[0] = x0, y0, z0

for i in range(len(t)-1):
    x_half = x[i] + 0.5*dt*(A*y[i] - A*x[i])
    y_half = y[i] + 0.5*dt*(-x[i]*z[i] + B*x[i] - y[i])
    z_half = z[i] + 0.5*dt*(x[i]*y[i] - C*z[i])
    x[i+1] = x[i] + dt*(A*y_half - A*x_half)
    y[i+1] = y[i] + dt*(-x_half*z_half + B*x_half - y_half)
    z[i+1] = z[i] + dt*(x_half*y_half - C*z_half)

fig, axes = plt.subplots(1, 3, figsize=(12,4))
plots = [('xy', x, y), ('xz', x, z), ('yz', y, z)]

for i, (title, X, Y) in enumerate(plots):
    ax = axes[i]
    ax.plot(X, Y)
    ax.set_xlabel('x' if i != 2 else 'y')
    ax.set_ylabel('y' if i == 0 else 'z')
plt.show()
