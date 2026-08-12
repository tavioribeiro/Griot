package org.tavioribeiro.griot

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform