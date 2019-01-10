package abc.def;

import abc.def.ui.HelperUIThread;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import org.apache.commons.codec.binary.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.*;

public class SimpleHelper {
    static private HelperUIThread uiThread = null;
    static public AbstractCard toChangeCard = null;
    static public AbstractRelic.RelicTier nextEliteRelicTier = null;
    static public String selectRelic = null;

    static public void switchUI() {
        if (uiThread == null) {
            uiThread = new HelperUIThread();
            uiThread.start();
        } else {
            uiThread.switchDisplay();
        }
    }

    static public void adjustCardReward(ArrayList<AbstractCard> rewardList) {
        if (toChangeCard != null) {
            // check if already exist
            boolean isExist = false;
            for (AbstractCard card : rewardList) {
                if (card.cardID.equals(toChangeCard.cardID)) {
                    isExist = true;
                    break;
                }
            }

            if (!isExist) {
                boolean isChanged = false;
                // check same rarity
                for (AbstractCard card : rewardList) {
                    if (card.rarity == toChangeCard.rarity) {
                        if (card.upgraded) {
                            toChangeCard.upgrade();
                        }
                        rewardList.remove(card);
                        rewardList.add(toChangeCard);
                        isChanged = true;
                        break;
                    }
                }

                if (!isChanged) {
                    AbstractCard removed = rewardList.remove(0);
                    if (removed.upgraded) {
                        toChangeCard.upgrade();
                    }
                    rewardList.add(toChangeCard);
                }
            }
            //toChangeCard = null;
        }
    }

    static AbstractRelic.RelicTier adjustEliteRelicTier(AbstractRelic.RelicTier raw) {
        AbstractRelic.RelicTier tier;
        if (nextEliteRelicTier != null) {
            tier = nextEliteRelicTier;
            nextEliteRelicTier = null;
        } else {
            tier = raw;
        }
        return tier;
    }

    static public ArrayList<String> removeDuplicate(ArrayList<String> list) {
        HashSet h = new HashSet(list);
        list.clear();
        list.addAll(h);
        return list;
    }

    static public void resetRelicPool() {
        if (CardCrawlGame.dungeon == null) {
            return;
        }
        ArrayList<String> commonRelicPool = new ArrayList<>();
        ArrayList<String> uncommonRelicPool = new ArrayList<>();
        ArrayList<String> rareRelicPool = new ArrayList<>();
        ArrayList<String> shopRelicPool = new ArrayList<>();
        ArrayList<String> bossRelicPool = new ArrayList<>();

        RelicLibrary.populateRelicPool(commonRelicPool, AbstractRelic.RelicTier.COMMON, AbstractDungeon.player.chosenClass);
        RelicLibrary.populateRelicPool(uncommonRelicPool, AbstractRelic.RelicTier.UNCOMMON, AbstractDungeon.player.chosenClass);
        RelicLibrary.populateRelicPool(rareRelicPool, AbstractRelic.RelicTier.RARE, AbstractDungeon.player.chosenClass);
        RelicLibrary.populateRelicPool(shopRelicPool, AbstractRelic.RelicTier.SHOP, AbstractDungeon.player.chosenClass);
        RelicLibrary.populateRelicPool(bossRelicPool, AbstractRelic.RelicTier.BOSS, AbstractDungeon.player.chosenClass);
        if (AbstractDungeon.floorNum >= 1) {
            for (final AbstractRelic r : AbstractDungeon.player.relics) {
                AbstractDungeon.relicsToRemoveOnStart.add(r.relicId);
            }
        }
        Collections.shuffle(AbstractDungeon.commonRelicPool, new java.util.Random(AbstractDungeon.relicRng.randomLong()));
        Collections.shuffle(AbstractDungeon.uncommonRelicPool, new java.util.Random(AbstractDungeon.relicRng.randomLong()));
        Collections.shuffle(AbstractDungeon.rareRelicPool, new java.util.Random(AbstractDungeon.relicRng.randomLong()));
        Collections.shuffle(AbstractDungeon.shopRelicPool, new java.util.Random(AbstractDungeon.relicRng.randomLong()));
        Collections.shuffle(AbstractDungeon.bossRelicPool, new java.util.Random(AbstractDungeon.relicRng.randomLong()));
        if (ModHelper.isModEnabled("Flight") || ModHelper.isModEnabled("Uncertain Future")) {
            AbstractDungeon.relicsToRemoveOnStart.add("WingedGreaves");
        }
        if (ModHelper.isModEnabled("Diverse")) {
            AbstractDungeon.relicsToRemoveOnStart.add("PrismaticShard");
        }
        if (ModHelper.isModEnabled("DeadlyEvents")) {
            AbstractDungeon.relicsToRemoveOnStart.add("Juzu Bracelet");
        }
        if (ModHelper.isModEnabled("Hoarder")) {
            AbstractDungeon.relicsToRemoveOnStart.add("Smiling Mask");
        }
        if (ModHelper.isModEnabled("Draft") || ModHelper.isModEnabled("SealedDeck") || ModHelper.isModEnabled("Shiny") || ModHelper.isModEnabled("Insanity")) {
            AbstractDungeon.relicsToRemoveOnStart.add("Pandora's Box");
        }
        AbstractDungeon.relicsToRemoveOnStart = removeDuplicate(AbstractDungeon.relicsToRemoveOnStart);
        for (final String remove : AbstractDungeon.relicsToRemoveOnStart) {
            Iterator<String> s = commonRelicPool.iterator();
            while (s.hasNext()) {
                final String derp = s.next();
                if (derp.equals(remove)) {
                    s.remove();
                    break;
                }
            }
            s = uncommonRelicPool.iterator();
            while (s.hasNext()) {
                final String derp = s.next();
                if (derp.equals(remove)) {
                    s.remove();
                    break;
                }
            }
            s = rareRelicPool.iterator();
            while (s.hasNext()) {
                final String derp = s.next();
                if (derp.equals(remove)) {
                    s.remove();
                    break;
                }
            }
            s = shopRelicPool.iterator();
            while (s.hasNext()) {
                final String derp = s.next();
                if (derp.equals(remove)) {
                    s.remove();
                    break;
                }
            }
            s = bossRelicPool.iterator();
            while (s.hasNext()) {
                final String derp = s.next();
                if (derp.equals(remove)) {
                    s.remove();
                    break;
                }
            }
        }
        List<String> finded = commonRelicPool.stream().filter(it -> !AbstractDungeon.commonRelicPool.contains(it)).collect(Collectors.toList());
        if (finded != null && finded.size() > 0) {
            AbstractDungeon.commonRelicPool.addAll(finded);
        }
        finded = uncommonRelicPool.stream().filter(it -> !AbstractDungeon.uncommonRelicPool.contains(it)).collect(Collectors.toList());
        if (finded != null && finded.size() > 0) {
            AbstractDungeon.uncommonRelicPool.addAll(finded);
        }
        finded = rareRelicPool.stream().filter(it -> !AbstractDungeon.rareRelicPool.contains(it)).collect(Collectors.toList());
        if (finded != null && finded.size() > 0) {
            AbstractDungeon.rareRelicPool.addAll(finded);
        }
        finded = shopRelicPool.stream().filter(it -> !AbstractDungeon.shopRelicPool.contains(it)).collect(Collectors.toList());
        if (finded != null && finded.size() > 0) {
            AbstractDungeon.shopRelicPool.addAll(finded);
        }
        finded = bossRelicPool.stream().filter(it -> !AbstractDungeon.bossRelicPool.contains(it)).collect(Collectors.toList());
        if (finded != null && finded.size() > 0) {
            AbstractDungeon.bossRelicPool.addAll(finded);
        }
        clearHasRelicInPool();
    }

    static public void clearHasRelicInPool() {
        for (final String remove : AbstractDungeon.relicsToRemoveOnStart) {
            Iterator<String> s = AbstractDungeon.commonRelicPool.iterator();
            while (s.hasNext()) {
                final String derp = s.next();
                if (derp.equals(remove)) {
                    s.remove();
                    break;
                }
            }
            s = AbstractDungeon.uncommonRelicPool.iterator();
            while (s.hasNext()) {
                final String derp = s.next();
                if (derp.equals(remove)) {
                    s.remove();
                    break;
                }
            }
            s = AbstractDungeon.rareRelicPool.iterator();
            while (s.hasNext()) {
                final String derp = s.next();
                if (derp.equals(remove)) {
                    s.remove();
                    break;
                }
            }
            s = AbstractDungeon.bossRelicPool.iterator();
            while (s.hasNext()) {
                final String derp = s.next();
                if (derp.equals(remove)) {
                    s.remove();
                    break;
                }
            }
            s = AbstractDungeon.shopRelicPool.iterator();
            while (s.hasNext()) {
                final String derp = s.next();
                if (derp.equals(remove)) {
                    s.remove();
                    break;
                }
            }
        }
    }

    static public void adjustRewardForSelect(ArrayList<RewardItem> rewardList) {
        // check if already exist
        for (int i = 0; i < rewardList.size(); i++) {
            RewardItem item = rewardList.get(i);
            if (toChangeCard != null) {
                if (item.type == RewardItem.RewardType.CARD) {
                    boolean isExist = false;
                    for (AbstractCard card : item.cards) {
                        if (card.cardID.equals(toChangeCard.cardID)) {
                            isExist = true;
                            if (card.canUpgrade()) {
                                card.upgrade();
                            }
                            break;
                        }
                    }
                    if (!isExist) {
                        boolean isChanged = false;
                        // check same rarity
                        for (AbstractCard card : item.cards) {
                            if (card.rarity == toChangeCard.rarity) {
                                if (card.upgraded) {
                                    toChangeCard.upgrade();
                                }
                                item.cards.remove(card);
                                item.cards.add(toChangeCard);
                                isChanged = true;
                                break;
                            }
                        }
                        if (!isChanged) {
                            AbstractCard removed = item.cards.remove(0);
                            if (removed.upgraded) {
                                toChangeCard.upgrade();
                            }
                            item.cards.add(toChangeCard);
                        }
                    }
                }
            }
        }

        if (selectRelic != null) {
            if (AbstractDungeon.player.hasRelic(selectRelic)) {
                selectRelic = null;
            } else {
                boolean isExist = false;
                for (int i = 0; i < rewardList.size(); i++) {
                    RewardItem item = rewardList.get(i);
                    if (item.type == RewardItem.RewardType.RELIC) {
                        if (item.relic.relicId.equals(selectRelic)) {
                            isExist = true;
                        }
                    }

                }
                if (!isExist) {
                    for (int i = 0; i < rewardList.size(); i++) {
                        RewardItem item = rewardList.get(i);
                        if (item.type == RewardItem.RewardType.RELIC) {
                            rewardList.set(i, new RewardItem(RelicLibrary.getRelic(selectRelic)));
                        }
                    }
                }
            }
            clearHasRelicInPool();
        }
    }

    public static void SetAllMonsterWeakDebuff(int magicNumber) {
        AbstractPlayer p = AbstractDungeon.player;
        Iterator var3 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();
        while (var3.hasNext()) {
            AbstractMonster mo = (AbstractMonster) var3.next();
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(mo, p, new WeakPower(mo, magicNumber, false), magicNumber, true, AbstractGameAction.AttackEffect.NONE));
        }
    }

    public static void SetAllMonsterVulnerableDebuff(int magicNumber) {
        AbstractPlayer p = AbstractDungeon.player;
        Iterator var3 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();
        while (var3.hasNext()) {
            AbstractMonster mo = (AbstractMonster) var3.next();
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(mo, p, new VulnerablePower(mo, magicNumber, false), magicNumber, true, AbstractGameAction.AttackEffect.NONE));
        }
    }

    public static void SetAllMonsterStrengthDebuff(int magicNumber) {
        AbstractPlayer p = AbstractDungeon.player;
        Iterator var3 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();
        while (var3.hasNext()) {
            AbstractMonster mo = (AbstractMonster) var3.next();
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(mo, p, new StrengthPower(mo, -magicNumber), -magicNumber));
        }
    }

    public static void RemoveCirclet(AbstractPlayer p) {
        p.relics.removeIf(r -> r.relicId.equals("Circlet") || r.relicId.equals("Red Circlet"));
    }

    public static void SetCampfireUIButtonPosition(ArrayList<AbstractCampfireOption> buttons) {
        if (buttons.size() == 5) {
            buttons.get(0).setPosition(800.0F * Settings.scale, 720.0F * Settings.scale);
            buttons.get(1).setPosition(1110.0F * Settings.scale, 720.0F * Settings.scale);
            buttons.get(2).setPosition(800.0F * Settings.scale, 450.0F * Settings.scale);
            buttons.get(3).setPosition(1110.0F * Settings.scale, 450.0F * Settings.scale);
            buttons.get(4).setPosition(950.0F * Settings.scale, 250.0F * Settings.scale);
        }
    }
}
