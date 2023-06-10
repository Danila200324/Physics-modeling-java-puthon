import numpy as np
import matplotlib.pyplot as plt

GRAVITY_CONSTANT = 9.8
SPHERE_RADIUS = 0.1
SPHERE_MASS = 1
SPHERE_MOMENT_OF_INERTIA = 2.0 / 5.0 * SPHERE_MASS * SPHERE_RADIUS * SPHERE_RADIUS
CYLINDER_RADIUS = 0.5
CYLINDER_LENGTH = 0.5
CYLINDER_MASS = 2
CYLINDER_MOMENT_OF_INERTIA = 1.0 / 12.0 * CYLINDER_MASS * (3 * CYLINDER_RADIUS * CYLINDER_RADIUS + CYLINDER_LENGTH * CYLINDER_LENGTH)
TIME_STEP = 0.01
ANGLE = np.radians(15)
FINAL_TIME = 5

time_list, sphere_position_list, cylinder_position_list, sphere_angle_list, cylinder_angle_list, sphere_potential_energy_list, sphere_kinetic_energy_list, sphere_total_energy_list, cylinder_potential_energy_list, cylinder_kinetic_energy_list, cylinder_total_energy_list = [], [], [], [], [], [], [], [], [], [], []

current_time, sphere_position, cylinder_position, sphere_velocity, cylinder_velocity, sphere_angular_velocity, cylinder_angular_velocity, sphere_angle, cylinder_angle, sphere_mass_radius2, cylinder_mass_radius2 = 0, 0, 0, 0, 0, np.radians(10), np.radians(20), 0, 0, SPHERE_MASS * SPHERE_RADIUS * SPHERE_RADIUS, CYLINDER_MASS * CYLINDER_RADIUS * CYLINDER_RADIUS

while current_time <= FINAL_TIME:
    sphere_acceleration = GRAVITY_CONSTANT * np.sin(ANGLE) / (1 + SPHERE_MOMENT_OF_INERTIA / sphere_mass_radius2)
    cylinder_acceleration = GRAVITY_CONSTANT * np.sin(ANGLE) / (1 + CYLINDER_MOMENT_OF_INERTIA / cylinder_mass_radius2)

    sphere_velocity_midpoint = sphere_velocity + 0.5 * sphere_acceleration * TIME_STEP
    cylinder_position_midpoint = cylinder_position + 0.5 * cylinder_velocity * TIME_STEP
    sphere_angular_velocity_midpoint = sphere_angular_velocity + 0.5 * sphere_angle * TIME_STEP
    cylinder_angular_velocity_midpoint = cylinder_angular_velocity + 0.5 * cylinder_angle * TIME_STEP

    sphere_velocity += sphere_acceleration * TIME_STEP
    cylinder_velocity += cylinder_acceleration * TIME_STEP
    sphere_position += sphere_velocity_midpoint * TIME_STEP
    cylinder_position += cylinder_position_midpoint * TIME_STEP
    sphere_angular_velocity += sphere_angle * TIME_STEP
    cylinder_angular_velocity += cylinder_angle * TIME_STEP
    sphere_angle += sphere_angular_velocity_midpoint * TIME_STEP
    cylinder_angle += cylinder_angular_velocity_midpoint * TIME_STEP

    sphere_potential_energy = SPHERE_MASS * GRAVITY_CONSTANT * sphere_position * np.sin(ANGLE)
    sphere_kinetic_energy = 0.5 * SPHERE_MASS * sphere_velocity * sphere_velocity + 0.5 * SPHERE_MOMENT_OF_INERTIA * sphere_angular_velocity * sphere_angular_velocity
    sphere_total_energy = sphere_potential_energy + sphere_kinetic_energy
    cylinder_potential_energy = CYLINDER_MASS * GRAVITY_CONSTANT * cylinder_position * np.sin(ANGLE)
    cylinder_kinetic_energy = 0.5 * CYLINDER_MASS * cylinder_velocity * cylinder_velocity + 0.5 * CYLINDER_MOMENT_OF_INERTIA * cylinder_angular_velocity * cylinder_angular_velocity
    cylinder_total_energy = cylinder_potential_energy + cylinder_kinetic_energy

    time_list.append(current_time)
    sphere_position_list.append(sphere_position)
    cylinder_position_list.append(cylinder_position)
    sphere_angle_list.append(np.degrees(sphere_angle))
    cylinder_angle_list.append(np.degrees(cylinder_angle))
    sphere_potential_energy_list.append(sphere_potential_energy)
    sphere_kinetic_energy_list.append(sphere_kinetic_energy)
    sphere_total_energy_list.append(sphere_total_energy)
    cylinder_potential_energy_list.append(cylinder_potential_energy)
    cylinder_kinetic_energy_list.append(cylinder_kinetic_energy)
    cylinder_total_energy_list.append(cylinder_total_energy)

    current_time += TIME_STEP

plt.figure(figsize=(15, 4))

plt.subplot(1, 3, 1)
plt.title('Position vs Time')
plt.plot(time_list, sphere_position_list, label='Sphere')
plt.plot(time_list, cylinder_position_list, label='Cylinder')
plt.xlabel('Time')
plt.ylabel('Position')
plt.legend()

plt.subplot(1, 3, 2)
plt.title('Angle vs Time')
plt.plot(time_list, sphere_angle_list, label='Sphere')
plt.plot(time_list, cylinder_angle_list, label='Cylinder')
plt.xlabel('Time')
plt.ylabel('Angle')
plt.legend()

plt.subplot(1, 3, 3)
plt.title('Energy vs Time')
plt.plot(time_list, sphere_potential_energy_list, label='Sphere (potential)')
plt.plot(time_list, sphere_kinetic_energy_list, label='Sphere (kinetic)')
plt.plot(time_list, sphere_total_energy_list, label='Sphere (total)')
plt.plot(time_list, cylinder_potential_energy_list, label='Cylinder (potential)')
plt.plot(time_list, cylinder_kinetic_energy_list, label='Cylinder (kinetic)')
plt.plot(time_list, cylinder_total_energy_list, label='Cylinder (total)')
plt.xlabel('Time')
plt.ylabel('Energy')
plt.legend()

plt.show()