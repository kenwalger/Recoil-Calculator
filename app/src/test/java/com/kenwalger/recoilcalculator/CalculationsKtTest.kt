package com.kenwalger.recoilcalculator

import org.junit.Assert
import org.junit.Assert.*
import org.junit.Test

class CalculationsKtTest {

    @Test
    fun calRecoilEnergyFromVarmintAls() {
        /*
        * rifleWeightPounds: Double,
        * bulletWeightGrains: Int,
        * powderWeightGrains: Double,
        * bulletVelocity: Int
        */

        // Set values from Varmintal's calculator site
        val rifleWeight = 8.0
        val bulletWeight = 100
        val powderWeight = 42.0
        val bulletVelocity = 2900

        // Call the function
        val result: Float = calRecoilEnergy(
            rifleWeightPounds = rifleWeight,
            bulletWeightGrains = bulletWeight,
            powderWeightGrains = powderWeight,
            bulletVelocity = bulletVelocity
        ).toFloat()

        // Check the result
        assertEquals( 9.42f, result, 0.005f)
    }

    @Test
    fun calRifleVelocityFromVarmintAls() {
//        rifleWeightPounds: Double,
//        bulletWeightGrains: Int,
//        powderWeightGrains: Double,
//        bulletVelocity: Int): Double

        val rifleWeight = 8.0
        val bulletWeight = 100
        val powderWeight = 42.0
        val bulletVelocity = 2900

        val result = calRifleVelocity(
            rifleWeightPounds = rifleWeight,
            bulletWeightGrains = bulletWeight,
            powderWeightGrains = powderWeight,
            bulletVelocity = bulletVelocity
        ).toFloat()

        assertEquals(8.7f, result,0.005f)
    }

    @Test
    fun calBulletAngularVelocityFromVarmintAls() {
        // twistRate: Int, velocity: Int

        val result = (calBulletAngularVelocity(twistRate = 10, velocity = 2900)* 30 / 3.14159265).toFloat()

        assertEquals(208800f, result,  0.005f)
    }

    @Test
    fun calBulletLinearMuzzleEnergyFromVarmintAls() {
        // bulletWeightGrains: Int, velocity: Int

        val result = calBulletLinearMuzzleEnergy(bulletWeightGrains = 100, velocity = 2900).toFloat()

        assertEquals(1867.51f, result, 0.005f)
    }

    @Test
    fun calBulletSpinEnergyFromVarmintAls() {
        // diameter: Double, mass: Int, velocity: Int, twist: Int

        val result = calBulletSpinEnergy(diameter = .243, mass = 100, velocity = 2900, twist = 10).toFloat()

        assertEquals(5.44f, result,  0.005f)
    }
}