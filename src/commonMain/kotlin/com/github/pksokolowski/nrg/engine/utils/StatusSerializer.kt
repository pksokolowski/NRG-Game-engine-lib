package com.github.pksokolowski.nrg.engine.utils

import com.github.pksokolowski.nrg.engine.Status
import kotlinx.serialization.*
import kotlinx.serialization.internal.StringDescriptor

@Serializer(forClass = Status::class)
object StatusSerializer : KSerializer<Status> {
    override val descriptor: SerialDescriptor =
        StringDescriptor.withName("GameState")

    override fun serialize(output: Encoder, obj: Status) {
        val str = toString(obj)
        output.encodeString(str)
    }

    override fun deserialize(input: Decoder): Status {
        val str = input.decodeString()
        return fromString(str)
    }

    private fun toString(status: Status) = status.run {
        val builder = StringBuilder()

        builder.append(
            when (status) {
                is Status.FINISHED -> {
                    "|FINISHED|${status.winner},${status.eval}"
                }
                else -> {
                    "|ONGOING|"
                }
            }
        )

        builder.toString()
    }

    private fun fromString(status: String): Status {
        val parts = status.split("|")

        val commonVars = parts[0].split(",")
        val type = parts[1]
        val typeSpecificVars = parts.getOrElse(2) { "" }.split(",")

        return when (type) {
            "FINISHED" -> {
                val winner = typeSpecificVars[0].let {
                    when (it) {
                        "HUMAN" -> Status.FINISHED.Player.HUMAN
                        "AI" -> Status.FINISHED.Player.AI
                        else -> Status.FINISHED.Player.NOBODY
                    }
                }
                val eval = typeSpecificVars[1].toInt()
                Status.FINISHED(winner, eval)
            }
            else -> Status.ONGOING
        }
    }
}
