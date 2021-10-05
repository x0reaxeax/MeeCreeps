package mcjty.meecreeps.gui;

import java.io.IOException;
 
import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.TextComponentString;

import java.util.regex.*;

public class GuiAskCoords extends GuiScreen {

    private GuiButton done_button;
    private int update_counter;

    public GuiTextField textfield;
    public static String player_input;
    
    
    @Override
    public void initGui() {
        textfield = new GuiTextField(1, this.fontRenderer, this.width / 2 - 68, this.height / 2 - 46, 137, 20);

        this.buttonList.clear();
        Keyboard.enableRepeatEvents(true);
        this.done_button = this.addButton(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 120, I18n.format("gui.done"))); 
        
        // 30,000,000 max xyz, 3 times + 3 minus (negative) signs + 2 separators + 3 decimal points + 3 decimal numbers
        textfield.setMaxStringLength(35);
        textfield.setText("");
        this.textfield.setFocused(true);
        textfield.setCanLoseFocus(true);
    }

    public void keyTyped(char c, int i) throws IOException {
        super.keyTyped(c, i);
        this.textfield.textboxKeyTyped(c, i);
       
    }
   
    public void onGuiClosed() {
        textfield.setFocused(false);
        Keyboard.enableRepeatEvents(false);
    }
   
    public void updateScreen() {
        ++this.update_counter;
        this.textfield.updateCursorCounter();
        player_input = textfield.getText();
    }
   
    protected void actionPerformed(GuiButton button) throws IOException {
        if(button.enabled) {
            if(button.id == 0) {
                this.mc.displayGuiScreen((GuiScreen)null);

                // (?<=[:,])[^;,]+
                // System.out.println("GUI_ACTION #1");

                // WIP only testing now.. continue here tomorrow..

                int counter = 0;
                char[] coordxyz = { 'X', 'Y', 'Z' };
                Pattern rgx_pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

                Matcher rgx_matcher = rgx_pattern.matcher(player_input);

                while(rgx_matcher.find()) {
                    if (counter < 3) {
                        System.out.println(coordxyz[counter] + ": " + rgx_matcher.group());
                    }
                    counter++;
                }
            }
        }
    }
   
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, "Enter XYZ Coords:", this.width / 2, 40, 16777215);
        this.textfield.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
   
    protected void mouseClicked(int x, int y, int btn) throws IOException {
        super.mouseClicked(x, y, btn);
        this.textfield.mouseClicked(x, y, btn);
        // System.out.println("GUI_ACTION #2");
    }
   
    public static String getPlayerInput() {
        return player_input;
    }

}