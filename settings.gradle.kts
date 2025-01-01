pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "RemoteDemo"
include(":app")
include(":remoteviewserver")
include(":remoteviewclient")
include(":remotesurfacehost")
include(":remotesurfaceservice")
include(":remotesurfacelib")
include(":embedserver")
include(":embedclient")
