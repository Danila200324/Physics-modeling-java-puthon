1import numpy as np
import matplotlib.pyplot as plt

def earth_moon(me, mm):

    ms = 2e30
    ntsteps = 12000
    dt = 0.001

    xe = np.zeros(ntsteps)
    xe[0] = 1
    ye = np.zeros(ntsteps)
    ye[0] = 0
    vex = 0
    vey = 6.27


    xm = np.zeros(ntsteps)
    xm[0] = 1.0027
    ym = np.zeros(ntsteps)
    ym[0] = 0
    vmx = 0
    vmy = 0.215 + vey

    for i in range(1, ntsteps):
        rm = np.sqrt(xm[i-1]**2 + ym[i-1]**2)
        re = np.sqrt(xe[i-1]**2 + ye[i-1]**2)
        rme = np.sqrt((xm[i-1] - xe[i-1])**2 + (ym[i-1] - ye[i-1])**2)

        vex = vex - 4 * np.pi**2 * xe[i-1] / re**3 * dt - 4 * np.pi**2 * (mm / ms) * (xe[i-1] - xm[i-1])/rme**3 * dt
        vey = vey - 4 * np.pi**2 * ye[i-1] / re**3 * dt - 4 * np.pi**2 * (mm / ms) * (ye[i-1] - xm[i-1])/rme**3 * dt
        vmx = vmx - 4 * np.pi**2 * xm[i-1] / rm**3 * dt - 4 * np.pi**2 * (me / ms) * (xm[i-1] - xe[i-1])/rme**3 * dt
        vmy = vmy - 4 * np.pi**2 * ym[i-1] / rm**3 * dt - 4 * np.pi**2 * (me / ms) * (ym[i-1] - ye[i-1])/rme**3 * dt

        xe[i] = xe[i-1] + vex * dt
        ye[i] = ye[i-1] + vey * dt
        xm[i] = xm[i-1] + vmx * dt
        ym[i] = ym[i-1] + vmy * dt

    return xe, ye, xm, ym

me = 5.97237e24
mm = 7.3477e22
xe, ye, xm, ym = earth_moon(me, mm)

# Plot results
plt.plot(xm, ym, 'r')
plt.plot(xe, ye, 'b')
plt.plot(0, 0, 'r.', markersize=50)
plt.plot(0, 0, 'y.', markersize=40)

plt.grid()
plt.axis('square')
plt.xlabel('X')
plt.ylabel('Y')
plt.ylim([-2, 2])
plt.xlim([-2, 2])
plt.title('Orbit of Earth and Moon around the Sun')
plt.legend(['Moon', 'Earth', 'Sun', 'Initial position'], loc='upper right')
moon_orbit = plt.Circle((xe[0], ye[0]), 0.0027, color='r', fill=False)
plt.gca().add_patch(moon_orbit)
plt.show()

