package hicuta.damager.config

import hicuta.damager.Damager
import hicuta.damager.DamagerHandler
import hicuta.damager.SerializableDamager
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.axay.kspigot.extensions.geometry.LocationArea
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

object DamagerConfiguration {
    val path = File("plugins/damager")
    val file = File(path,"damagers.yml")
    val config = YamlConfiguration.loadConfiguration(file)

    init {
        if(!path.exists()) path.mkdir()
        if(!file.exists()) file.createNewFile()
    }


    fun addDamager(damager:Damager){
        val loc1 = damager.box.loc1
        val loc2 = damager.box.loc2
        val serializableDamager = SerializableDamager(damager.name, damager.damage, damager.ticks,loc1.x, loc1.y,loc1.z,loc2.x,loc2.y,loc2.z ,loc1.world.name)
        config.set("damagers.${damager.name}", Json.encodeToString(serializableDamager))
        val list = config.getStringList("damagers.names")
        list.add(damager.name)
        config.set("damagers.names",list)
        config.save(file)
        config.load(file)
    }

    fun removeDamager(name: String){
        config.set("damagers.${name}", null)
        val list = config.getStringList("damagers.names")
        list.remove(name)
        config.set("damagers.names", list)
        config.save(file)
        config.load(file)
    }

    fun containsDamager(name: String): Boolean {
        return config.contains("damagers.${name}")
    }

    fun loadDamagers(){
        val list = config.getStringList("damagers.names")
        for(d in list){
            val json = Json.decodeFromString<SerializableDamager>(config.getString("damagers.$d").toString())
            val locArea= LocationArea(Location(Bukkit.getWorld(json.world), json.xA, json.yA, json.zA), Location(Bukkit.getWorld(json.world), json.xB, json.yB, json.zB))
            DamagerHandler.damagers.add(Damager(json.name,json.damage, json.ticks, locArea))
        }
    }




}