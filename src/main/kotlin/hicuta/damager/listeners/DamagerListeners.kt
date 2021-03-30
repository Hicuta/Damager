package hicuta.damager.listeners

import hicuta.damager.DamagerHandler
import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.bukkit.heal
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.entity.ItemSpawnEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.inventory.ItemStack

object DamagerListeners {

    val inDamager = hashSetOf<Player>()
    val damagerItems = arrayListOf(
        Material.BOWL,
        Material.MUSHROOM_STEW,
        Material.RED_MUSHROOM,
        Material.BROWN_MUSHROOM,
        Material.STONE_SWORD
    )

    init {
        listen<PlayerMoveEvent> {
            if (inDamager.contains(it.player) && !isInDamager(it.to)) {
                it.player.inventory.clear()
                it.player.heal()
                inDamager.remove(it.player)
                return@listen
            }
            if (isInDamager(it.to) && !inDamager.contains(it.player)) {
                setSoupInventory(it.player)
                inDamager.add(it.player)
            }
        }

        listen<ItemSpawnEvent> {
            if (isInDamager(it.location) && damagerItems.contains(it.entity.itemStack.type)) {
                it.entity.remove()
            }
        }

        listen<PlayerDeathEvent> {
            if (isInDamager(it.entity.location)) {
                it.drops.clear()
                it.setShouldDropExperience(false)
                it.deathMessage(null)
                inDamager.remove(it.entity)
            }
        }
    }

    fun setSoupInventory(p: Player) {
        p.inventory.clear()
        p.inventory.setItem(0, ItemStack(Material.STONE_SWORD))
        p.inventory.setItem(13, ItemStack(Material.BOWL, 64))
        p.inventory.setItem(14, ItemStack(Material.RED_MUSHROOM, 64))
        p.inventory.setItem(15, ItemStack(Material.BROWN_MUSHROOM, 64))
        for (i in 1 until 32) {
            p.inventory.addItem(ItemStack(Material.MUSHROOM_STEW))
        }
    }

    fun isInDamager(loc: Location): Boolean {
        DamagerHandler.damagers.forEach {
            return it.box.isInArea(loc)
        }
        return false
    }

}