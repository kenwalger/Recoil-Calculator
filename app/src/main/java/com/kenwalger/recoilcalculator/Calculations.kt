package com.kenwalger.recoilcalculator

import kotlin.math.pow

// Rifle Recoil Energy Calculation (ft-lb)
// RECOIL = .5 * (BulletMass * Bullet Velocity + Mass of Powder * Velocity of Gas) ^2 / Rifle Mass
fun calRecoilEnergy(rifleWeightPounds: Double,
                    bulletWeightGrains: Int,
                    powderWeightGrains: Double,
                    bulletVelocity: Int) : Double {
    val gravityForce = 32.16
    val bulletMass = bulletWeightGrains / (7000 * gravityForce)
    val powderMass = powderWeightGrains / (7000 * gravityForce)
    val velocityOfGas = 4700
    val rifleMass = rifleWeightPounds / gravityForce

    val recoilEnergy =
        .5 * (bulletMass * bulletVelocity + powderMass * velocityOfGas).pow(2) / rifleMass

    return recoilEnergy
}

// Rifle Velocity Calculation (fps)
fun calRifleVelocity(rifleWeightPounds: Double,
                     bulletWeightGrains: Int,
                     powderWeightGrains: Double,
                     bulletVelocity: Int): Double {
    val gravityForce = 32.16                                                                                       // 32.16 lbm*ft/(lbf*sec^2)
    val rifleMass = rifleWeightPounds / gravityForce                                                               // Lbm
    val bulletMass = bulletWeightGrains / (7000 * gravityForce)                                                    // Lbm
    val powderMass = powderWeightGrains / (7000 * gravityForce)                                                    // Lbm
    val velocityOfGas = 4700                                                                            // fps

    val rifleVelocity = (bulletMass * bulletVelocity + powderMass * velocityOfGas) / rifleMass

    return rifleVelocity
}

// Bullet Angular Velocity (Radians per second)
// VIN = bulletVelocity * 12
// TWIST = 1 / barrel twist
// Angular Velocity = VIN * TWIST * 2 * PI
fun calBulletAngularVelocity(twistRate: Int, velocity: Int): Double {
    val pi = 3.14159265

    val twist: Double= (1 / twistRate.toDouble())                                                                           // rev/inch
    val bulletVelocityInchesPerSecond = velocity * 12                                                            // in/sec
    val bulletAngularVelocity = bulletVelocityInchesPerSecond * twist * 2 * pi                                   // radians/sec

    return bulletAngularVelocity

}

// Bullet Linear Muzzle Energy Calculation (ft-lb)
// KE = .5 * BM * VIN ^ 2 / 12:     REM Bullet linear energy (ft-lb)
fun calBulletLinearMuzzleEnergy(bulletWeightGrains: Int, velocity: Int ): Double {
    val gravityForce = 386                                                                                  // Gravity 386 (lbm-in)/(lbf-sec^2)

    val bulletMass: Double = (bulletWeightGrains / (7000 * gravityForce.toDouble()))                                                      // lbm
    val bulletVelocityInchesPerSecond = velocity * 12                                                            // in/sec
    val bulletLinearMuzzleEnergy = .5 * bulletMass * (bulletVelocityInchesPerSecond * bulletVelocityInchesPerSecond) / 12       // ft-lb

    return bulletLinearMuzzleEnergy
}

// Bullet Spin Energy Calculation (ft-lb)
// Bullet Angular Energy (Spin Energy) = .5 * BulletMassMOI * Bullet Angular Velocity^2 / 12
fun calBulletSpinEnergy(diameter: Double, mass: Int, velocity: Int, twist: Int): Double {
    val gravityForce = 386
    val radius = diameter / 2
    val bulletMass = mass / (7000 * gravityForce.toDouble())
    val bulletMassMOI = .5 * bulletMass * radius.pow(2)

    val bulletAngularEnergy = calBulletAngularVelocity(twist, velocity)

    val bulletSpinEnergy = (.5 * bulletMassMOI * bulletAngularEnergy.pow(2)) / 12

    return bulletSpinEnergy
}