# DeadEssentials
**Provides essential features (fixes and addons) for your 1.6.4 Crafting Dead server.**  
**By default, it enables all its fixes. For more information, check the [documentation](https://github.com/Arzio/DeadEssentials/#-dependencies)**

## 🔧 Installation:

1. Download the latest DeadEssentials version at the [releases page](https://github.com/Arzio/DeadEssentials/releases).
2. Put the downloaded .jar file in your server's plugin folder.
3. Download and put [all the dependencies](https://github.com/Arzio/DeadEssentials#-dependencies) in your server's plugin folder.

### 📦 Dependencies:
- [WorldGuard](https://media.forgecdn.net/files/719/257/worldguard-5.8.jar). Recommended version: 5.8
- [WGCustomFlags](https://media.forgecdn.net/files/720/514/WorldGuard_Custom_Flags.zip). Recommended version: 1.6
 
Aditionally, WorldGuard 5.8 needs [WorldEdit 5.5.8](https://media.forgecdn.net/files/739/931/worldedit-5.5.8.jar).

## 📗 Documentation
This plugin still needs more documentation, including a better README file.  
If you want to help with it, you are welcome.

## 🔨 Building the project

###  How to download the dependencies
- Clone or download this repository
- Open your shell (or cmd) and navigate to the same place as the build.gradle is located
- Run `gradlew downloadFiles` and wait until it is done
- The downloaded files will be located at `/build/downloaded-files/` folder

### How to build
- Clone or download this repository
- Open your shell (or cmd) and navigate to the same place as the build.gradle is located
- Run `gradlew build` and wait until it is done
- The jar file will be located at `/build/libs/` folder
