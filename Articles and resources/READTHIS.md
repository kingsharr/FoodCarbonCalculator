I have added 2 Java and 2 XML files here. 
You need to use 1 drawable which is no_image_icon.xml. You can find under the drawables folder.
The depemdecies you can find in the README.md. Use the 3 dependencies which is under the comment News.
Lastly, you have to do this in your settings.gradle.kts:
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven ( "https://jitpack.io" )
    }
}
The above method should look like this. You should just add this "maven ( "https://jitpack.io" )" there in that method which can be found in your settings.gradle.kts
