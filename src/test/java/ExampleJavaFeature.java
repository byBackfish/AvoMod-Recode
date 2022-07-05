import de.bybackfish.avomod.annotation.*;
import de.bybackfish.avomod.features.FeatureInherit;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Feature(
    name = "ExampleJavaFeature", description = "This is an example feature for Java", displayName = "ExampleJavaFeature"
)
public class ExampleJavaFeature extends FeatureInherit {

    @Setting(
        displayName = "ExampleSetting",
        type = SettingType.STRING
    )
    private String displayText = "Hello!";

    @OnInit
    public void onInit(){
        System.out.println("ExampleJavaFeature was initialized");
        //add init code here
    }

    @OnEnable
    public void onEnable(){
        System.out.println("ExampleJavaFeature was enabled | Current display Text: " + displayText);
        //add enable code here
    }

    @OnDisable
    public void onDisable(){
        System.out.println("ExampleJavaFeature was disabled");
        //add disable code here
    }
    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event){
        System.out.println("ExampleJavaFeature received chat: " + event.getMessage().getUnformattedText());

        //do stuff
    }
}
