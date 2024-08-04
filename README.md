# Rifle Recoil Calculator

![GitHub last commit (branch)](https://img.shields.io/github/last-commit/kenwalger/Recoil-Calculator/main)

This is an Android based (Kotlin + Jetpack Compose) app that computes simple rifle recoil and
bullet energy.

User input is done on the left side of the screen (lighter gray boxes) and the calculations are then
displayed on the right side of the screen (darker gray boxes). 

Based on the Rifle Recoil Energy calculation, the box that value is displayed in is shown in one of
five colors representing a "Comfort Level" of the recoil for an average adult shooter. The categories
and values are as follows:

+ Enjoyable: 0 - 9.999999999 ft. lbs. of energy
+ Comfortable: 10 - 19.999999999 ft. lbs.
+ Uncomfortable: 20 - 29.999999999 ft. lbs.
+ I'd Rather Not: 30 - 49.999999999 ft. lbs.
+ Call a Medic: 50+ ft. lbs.

If you are taking the recoil energy into consideration for a new, or recoil sensitive, shooter, the
"New Shooter" switch option reduces the above values by half. Therefore, new shooter cateogies are
as follows:

+ Enjoyable: 0 - 4.999999999 ft. lbs. of energy
+ Comfortable: 5 - 9.999999999 ft. lbs.
+ Uncomfortable: 10 - 14.999999999 ft. lbs.
+ I'd Rather Not: 15 - 25.999999999 ft. lbs.
+ Call a Medic: 25+ ft. lbs.

While there are exceptions to every rule, I believe that these general guidelines for comfort level
are a decent starting point when exploring rifle + cartridge combinations.

## Screenshot
<img src="https://github.com/kenwalger/Recoil-Calculator/blob/main/app/src/main/res/drawable/app_screen_shot_colors.png" width="500" alt="App Screen Shot">

### Using the New Shooter option
<img src="https://github.com/kenwalger/Recoil-Calculator/blob/main/app/src/main/res/drawable/screen_shot_demo.gif" width="500" alt="Demo of 'New Shooter' Option">

#### Based on the DOS and Java Script Recoil Calculators from [Varmint Al's Shooting Page](https://www.varmintal.com/ashot.htm#Calculate_Recoil)