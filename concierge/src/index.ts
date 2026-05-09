import {
    Client,
    GatewayIntentBits,
    Events
} from "discord.js";

import dotenv from "dotenv";
import axios from "axios";

dotenv.config();

const client = new Client({
    intents: [
        GatewayIntentBits.Guilds,
        GatewayIntentBits.GuildMessages,
        GatewayIntentBits.MessageContent,
        GatewayIntentBits.DirectMessages
    ]
});

client.once(Events.ClientReady, () => {
    console.log(`Logged in`);
});

client.on(Events.MessageCreate, async (message) => {
    if (message.author.bot) return;

    // Optional:
    // only respond if mentioned
    if (!message.mentions.has(client.user!)) {
        return;
    }

    try {
        await message.channel.sendTyping();

        const cleaned = message.content.replace(
            `<@${client.user?.id}>`,
            ""
        );
       
            
        console.log("mandou mensagem")
        await axios.post(process.env.N8N_WEBHOOK!, {
            textmessage: message.content,
            user: message.author.username,
        });
    

    } catch (err) {
        console.error(err);

        await message.reply(
            "Failed to contact AI service."
        );
    }
});

client.login(process.env.DISCORD_TOKEN);
