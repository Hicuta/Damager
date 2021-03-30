package hicuta.damager

import hicuta.damager.config.DamagerConfiguration
import hicuta.damager.utils.isInt
import net.axay.kspigot.extensions.geometry.LocationArea
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object DamagerCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (!sender.hasPermission("damager.*")) return false
        if (args.size == 2 && args[0].toUpperCase() == "REMOVE") {
            if (DamagerConfiguration.containsDamager(args[1])) {
                try {
                    DamagerConfiguration.removeDamager(args[1])
                    DamagerHandler.damagers.forEach {
                        if (it.name.equals(args[1], ignoreCase = true)) DamagerHandler.damagers.remove(it)
                        it.id?.cancel()
                        DamagerHandler.restartDamagers()
                    }
                } catch (e: Exception) {
                }
                sender.sendMessage("${ChatColor.GREEN}Damager ${ChatColor.RED} ${args[1]} ${ChatColor.GREEN} removed")
            } else sender.sendMessage("${ChatColor.RED}Damager ${args[1]} dont exist")
        } else if (args.size == 10 && args[0].toUpperCase() == "ADD") {
            if (!(isInt(args[2]) && isInt(args[3]) && isInt(args[4]) && isInt(args[5]) && isInt(args[6]) && isInt(
                    args[7]) && isInt(args[8]) && isInt(args[9]))
            ) {
                sender.sendMessage("Please make sure that all parameters after Name are Integers")
                return false
            }
            if (DamagerConfiguration.containsDamager(args[1])) {
                sender.sendMessage("${ChatColor.RED}This Damager already exist")
                return false
            }
            val damager = Damager(args[1],
                args[2].toDouble(),
                args[3].toInt(),
                LocationArea(Location((sender as Player).location.world,
                    args[4].toDouble(),
                    args[5].toDouble(),
                    args[6].toDouble()),
                    Location(sender.location.world,
                        args[7].toDouble(),
                        args[8].toDouble(),
                        args[9].toDouble())))
            DamagerHandler.damagers.add(damager)
            DamagerConfiguration.addDamager(damager)
            sender.sendMessage("Added Damager")
            DamagerHandler.restartDamagers()
        } else {
            sender.sendMessage("${ChatColor.AQUA}/Damager remove <Name>")
            sender.sendMessage("${ChatColor.AQUA}/Damager add <Name> <damage> <Time in ticks> <First X> <First Y> <First Z> <Second X> <Second Y> <Second Z>")
            return false
        }
        return true
    }
}


