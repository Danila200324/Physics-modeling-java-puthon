import numpy as np
import matplotlib.pyplot as plt

t0, tf, dt = 0, 15, 0.01
A, B, C = 10, 25, 8 / 3
x0, y0, z0 = 1, 1, 1
t = np.arange(t0, tf + dt, dt)
x, y, z = np.zeros_like(t), np.zeros_like(t), np.zeros_like(t)
x[0], y[0], z[0] = x0, y0, z0


def f(x, y, z):
    dxdt = A * y - A * x
    dydt = -x * z + B * x - y
    dzdt = x * y - C * z
    return dxdt, dydt, dzdt


for i in range(len(t) - 1):
    k1x, k1y, k1z = f(x[i], y[i], z[i])
    k2x, k2y, k2z = f(x[i] + 0.5 * dt * k1x, y[i] + 0.5 * dt * k1y, z[i] + 0.5 * dt * k1z)
    k3x, k3y, k3z = f(x[i] + 0.5 * dt * k2x, y[i] + 0.5 * dt * k2y, z[i] + 0.5 * dt * k2z)
    k4x, k4y, k4z = f(x[i] + dt * k3x, y[i] + dt * k3y, z[i] + dt * k3z)
    x[i + 1] = x[i] + (dt / 6) * (k1x + 2 * k2x + 2 * k3x + k4x)
    y[i + 1] = y[i] + (dt / 6) * (k1y + 2 * k2y + 2 * k3y + k4y)
    z[i + 1] = z[i] + (dt / 6) * (k1z + 2 * k2z + 2 * k3z + k4z)

fig, axes = plt.subplots(1, 3, figsize=(12, 4))
plots = [('xy', x, y), ('xz', x, z), ('yz', y, z)]

for i, (title, X, Y) in enumerate(plots):
    ax = axes[i]
    ax.plot(X, Y)
    ax.set_xlabel('x' if i != 2 else 'y')
    ax.set_ylabel('y' if i == 0 else 'z')

plt.show()
