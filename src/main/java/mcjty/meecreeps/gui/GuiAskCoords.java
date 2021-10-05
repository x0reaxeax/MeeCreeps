package mcjty.meecreeps.gui;

import java.io.IOException;
 
import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;

import mcjty.meecreeps.MeeCreeps;
import mcjty.meecreeps.setup.GuiProxy;
import mcjty.meecreeps.teleport.TeleportDestination;

import java.util.regex.*;
import java.lang.Math;

public class GuiAskCoords extends GuiScreen {

    private GuiButton done_button;
    private int update_counter;

    public GuiTextField textfield;
    public static String player_input;
    
    
    @Override
    public void initGui() {
        textfield = new GuiTextField(1, this.fontRenderer, this.width / 2 - 68, (this.height / 2) / 2, 137, 20);

        this.buttonList.clear();
        Keyboard.enableRepeatEvents(true);
        this.done_button = this.addButton(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 120, I18n.format("gui.done"))); 
        
        // 30,000,000 max xyz 3 times + 3 minus (negative) signs + 3 separators + 3 decimal points + 3 times 3 decimal numbers + 1 FacingSide
        // (8 * 3) + (3 * 3) + 3 + 3 + (3 * 3) + 1 = 49
        textfield.setMaxStringLength(49);
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

                int counter = 0;    // for looping through xyz
                char[] coordxyz = { 'X', 'Y', 'Z' };
                
                // extract integers and/or doubles. count for negative sign
                Pattern rgx_pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

                Matcher rgx_matcher = rgx_pattern.matcher(player_input);

                double xyzvec[] = { 0, 0, 0 };

                while(rgx_matcher.find()) {
                    if (counter < 3) {
                        // string to double
                        xyzvec[counter] = Double.parseDouble(rgx_matcher.group());
                    }
                    counter++;
                }
                
                if (counter != 3) {
                    // System.out.println("[Debug] INCORRECT XYZ");
                    return;
                }

                // 6 sides - [D]own, [U]p, [W]est, [E]ast, [N]orth, [S]outh
                char[] facingsides = { 'D', 'U', 'W', 'E', 'N', 'S' };
                EnumFacing[] uglymatch = { EnumFacing.DOWN, EnumFacing.UP, EnumFacing.WEST, EnumFacing.EAST, EnumFacing.NORTH, EnumFacing.SOUTH };

                EnumFacing side = null;
                for (int i = 0; i < 6; i++) {
                    // match input to EnumFacing side
                    if (player_input.indexOf(facingsides[i]) != -1) {
                        side = uglymatch[i];
                        break;
                    }
                }

                // default side is UP
                if (side == null) {
                    side = EnumFacing.UP;
                }

                // Negative Y causes instant crash, so let's wrap it in abs()
                BlockPos dest_block_xyz = new BlockPos(xyzvec[0], Math.abs(xyzvec[1]), xyzvec[2]);
                GuiAskName.destination = new TeleportDestination("", mc.world.provider.getDimension(), dest_block_xyz, side);
                mc.player.openGui(MeeCreeps.instance, GuiProxy.GUI_ASKNAME, mc.world, (int) Math.round(xyzvec[0]), (int) Math.round(xyzvec[1]), (int) Math.round(xyzvec[2]));
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
    }
   
    public static String getPlayerInput() {
        return player_input;
    }

}