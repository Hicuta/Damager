package hicuta.damager

import hicuta.damager.config.DamagerConfiguration
import hicuta.damager.listeners.DamagerListeners
import hicuta.damager.listeners.SoupHealing
import net.axay.kspigot.extensions.bukkit.register
import net.axay.kspigot.main.KSpigot

open class InternalMainClass : KSpigot() {

    override fun startup() {

        DamagerCommand.register("damager")
        DamagerConfiguration.loadDamagers()
        SoupHealing
        DamagerListeners

        DamagerHandler.enable()
    }
}


