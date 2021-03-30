package hicuta.damager

import kotlinx.serialization.Serializable
import net.axay.kspigot.extensions.geometry.LocationArea
import net.axay.kspigot.runnables.KSpigotRunnable
import net.axay.kspigot.runnables.sync
import net.axay.kspigot.runnables.task
import org.bukkit.Bukkit
import org.jetbrains.annotations.NotNull

data class Damager(
    val name: String,
    val damage: Double,
    val ticks: Int,
    val box: LocationArea,
    var id: KSpigotRunnable? = null,
)

@Serializable
data class SerializableDamager(
    val name: String,
    val damage: Double,
    val ticks: Int,
    val xA: Double,
    val yA: Double,
    val zA: Double,
    val xB: Double,
    val yB: Double,
    val zB: Double,
    val world: @NotNull String,
)

object DamagerHandler {
    val damagers = arrayListOf<Damager>()


    fun enable() {
        startDamagers()
    }

    private fun startDamagers() {
        for (damager in damagers) {
            damager.id = task(sync = false,period = damager.ticks.toLong()) {
                for (p in Bukkit.getOnlinePlayers()) {
                    if (damager.box.isInArea(p.location)) {
                        p.noDamageTicks = 0
                        sync { p.damage(damager.damage) }
                    }
                }
            }
        }
    }

    fun restartDamagers() {
        for (damager in damagers) {
            damager.id?.cancel()
        }
        startDamagers()
    }
}