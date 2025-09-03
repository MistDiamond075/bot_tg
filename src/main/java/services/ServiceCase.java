package services;

import configuration.ConfApp;
import exception.CaseResultException;
import exception.PropertyException;
import utils.Pair;
import utils.Parser;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ServiceCase {
    private static final Logger log = Logger.getLogger(ServiceCase.class.getName());
    private final int defaultMaxResultsPerRequest=10;
    private final boolean guaranteeEnabled;
    private final Map<String,Integer> guarantees;
    private final Map<String,Float> rates;

    public ServiceCase() throws CaseResultException {
        guarantees=Map.copyOf(setGuarantee());
        rates=Map.copyOf(setRates());
        guaranteeEnabled=!guarantees.isEmpty();
    }

    public String createCaseResult() throws CaseResultException {
        int width,height;
        int maxResults;
        try {
            maxResults = Integer.parseInt(ConfApp.get("case.result.maxResultsPerRequest"));
        }catch(NumberFormatException|NullPointerException e){
            log.severe("an error occurred when parsing max results per request from config. Cause:"+e.getMessage()+"\nUsing default value");
            maxResults=defaultMaxResultsPerRequest;
        }
        CaseResult caseResult=new CaseResult(guaranteeEnabled ? false : null);
        try{
            currentRollResults = new String[Integer.parseInt(ConfApp.get("case.result.maxResultsPerRequest"))];
            width=Integer.parseInt(ConfApp.get("case.result.finalImage.width"));
            height=Integer.parseInt(ConfApp.get("case.result.finalImage.height"));
        }catch (NumberFormatException | NullPointerException e){
            throw new CaseResultException(e);
        }
        BufferedImage result = new BufferedImage(, 440, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = result.getGraphics();
        try {
            String[] images = new String[10];
            int k = 0;
            for (int i = 0; i < images.length; i++) {
                images[i] = getRandomSingleResult();
            }
            int x = 0;
            int y = 0;
            for (String image : images) {
                BufferedImage img;
                try {
                    img = ImageIO.read(new File(image));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if (k < 10) {
                    currentRollResults[k] = new File(image).getName();
                }
                graphics.drawImage(img, x, y, new Color(100, 0, 100, 0), null);
                x += 88;
                if (x > result.getWidth()) {
                    x = 0;
                    y += img.getHeight();
                }
                k++;
            }
        }finally {
            graphics.dispose();
        }
        try {
            ImageIO.write(result,"png",new File("files/pics/result"+uid+".png"));
        }
        catch (IOException e) {throw new RuntimeException(e);}
        return "./files/pics/result"+uid+".png";
    }

    private String getRandomSingleResult(CaseResult result,int maxResults) throws CaseResultException {
        String res = "";
        float randValue = (float) Math.random();
        if ((result.getCurrentCase() == 9) && (randValue > 0.16)) {
            randValue =guaranteeEnabled && result.getGuarantee() ? (float)0.10 : randValue;
        }
        if (randValue > 0.16) {
            int randPic = (int) (Math.random() * star3Img.size());
            res = star3Img.get(randPic);
            result.updateGuarantee(true);
            if (result.getCurrentCase() < maxResults) result.incrementCurrentCase();
        } else if ((randValue <= 0.16) && (randValue > 0.03)) {
            int randPic = (int) (Math.random() * star4Img.size());
            res = star4Img.get(randPic);
            if (!star5Indicator) star4Indicator = true;
            result.updateGuarantee(true);
            if (result.getCurrentCase() < maxResults) result.incrementCurrentCase();
        } else if (randValue <= 0.03) {
            int randPic = (int) (Math.random() * star5Img.size());
            res = star5Img.get(randPic);
            star5Indicator = true;
            star4Indicator = false;
            result.updateGuarantee(true);
            if (result.getCurrentCase() < maxResults) result.incrementCurrentCase();
        }
        return res;
    }

    private Map<String,Integer> setGuarantee() throws CaseResultException {
        Map<String,Integer> guaranteesMap= new HashMap<>();
        try {
            String configPropertyRaw = ConfApp.get("case.result.guarantee");
            if(configPropertyRaw!=null) {
                List<Pair<String, String>> pairs = Parser.parseAppProperties(configPropertyRaw);
                for (Pair<String, String> pair : pairs) {
                    guaranteesMap.put(pair.first, Integer.parseInt(pair.second));
                }
            }
        } catch (PropertyException | NullPointerException | NumberFormatException e) {
            throw new CaseResultException(e);
        }
        return guaranteesMap;
    }

    private Map<String,Float> setRates() throws CaseResultException {
        Map<String,Float> ratesMap= new LinkedHashMap<>();
        try {
            String configPropertyRaw = ConfApp.get("case.result.rates");
            List<Pair<String, String>> pairs = Parser.parseAppProperties(configPropertyRaw);
            pairs=pairs.stream()
                    .sorted(Comparator.comparingInt(x -> Integer.parseInt(x.second)))
                    .toList();
            for (Pair<String, String> pair : pairs) {
                ratesMap.put(pair.first, Float.parseFloat(pair.second));
            }
        } catch (PropertyException | NullPointerException | NumberFormatException e) {
            throw new CaseResultException(e);
        }
        return ratesMap;
    }

    private static class CaseResult{
        private Integer currentCase;
        private final List<byte[]> results;
        private Boolean guarantee;

        public CaseResult(Boolean guarantee) {
            currentCase=0;
            results=new ArrayList<>();
            this.guarantee = guarantee;
        }

        public Boolean getGuarantee() {
            return guarantee!=null ? guarantee : false;
        }

        public void updateGuarantee(boolean guarantee) {
            if(this.guarantee!=null){
                this.guarantee=guarantee;
            }
        }

        public List<byte[]> getResults() {
            return results;
        }

        public Integer getCurrentCase() {
            return currentCase;
        }

        public void incrementCurrentCase() {
            currentCase++;
        }
    }
}
