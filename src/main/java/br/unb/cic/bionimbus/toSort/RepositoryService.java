/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.unb.cic.bionimbus.toSort;

import br.unb.cic.bionimbus.config.BioNimbusConfig;
import br.unb.cic.bionimbus.plugin.PluginInfo;
import br.unb.cic.bionimbus.plugin.PluginService;
import br.unb.cic.bionimbus.services.AbstractBioService;
import br.unb.cic.bionimbus.services.messaging.CloudMessageService;
import br.unb.cic.bionimbus.services.messaging.CuratorMessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;

/**
 *
 * @author will
 * O nome da classe é temporario, assim como sua localização
 * 
 * Dados disponiveis atraves de metodos get
 */
public class RepositoryService extends AbstractBioService {
    
    public enum InstanceType {
        AMAZON_LARGE,
        PERSONAL,
        LABID_I7
    }
    
    @Inject
    public RepositoryService(final CloudMessageService cms) {
        this.cms = cms;
    }
    
    // TODO: deve haver uma classe basica contendo as informações de instancias
    // pergunta: qual é a nomeclatura para uma instancia de infra que não foi ativada e 
    //    qual é a nomeclatura para uma instancia ativa (executando algo)
//    public List<Instances> getInstancesList() {
//        // garante que a lista retornada pode ser a referencia atual, não precisando ser uma copia
//        return Collections.unmodifiableList(null);
//    }
    
    /**
     * Get instance cost from zookeeper cost
     *
     * MOCKED
     * 
     * @param type type of instance (e.g. AMAZON_LARGE)
     * @return cost of the input type instance
     */
    public double getInstanceCost(InstanceType type) {
        switch(type) {
            case LABID_I7:
                return 0.35d;
            case PERSONAL:
                return 0.12d;
            default:
                return -1d;     
        }
    }
    
    /**
     * Returns from zookeeper the list of modes of cycles necessary 
     * to execute a given service
     * List is updated lazily
     * 
     * MOCKED
     * 
     * @param serviceId id of requested service
     * @return 
     */
    public List<Double> getTaskHistory (Long serviceId) {
//        NavigableMap<Long, Long> currentHistory = new TreeMap<Long, Long>();
        List<Double> maximas = new ArrayList<Double>();
//
//        // check if task is supported
//        if(!cms.getZNodeExist(PREFIX_TASK+taskId, false)) {
//            // problem: task not supported
//            Log.warn("task not suported: task_" + taskId);
//            return null;
//        }
//        
//        // get histogram from task taskId
//        for(String task : cms.getChildren(PREFIX_TASK+taskId, null)) {
//            String count = cms.getData(PREFIX_TASK+taskId+SEPARATOR+task+COUNT, null);
//            String intervalStart = cms.getData(PREFIX_TASK+taskId+SEPARATOR+task+START, null);
//            currentHistory.put(Long.valueOf(intervalStart), Long.valueOf(count));
//        }
        
        // apply moving average
        
        
        // get all local maximas
        
        // MOCK
        switch(serviceId.intValue()) {
            case 1:
                maximas.add(5000000000d);
                maximas.add(20000000000d);
                maximas.add(35000000000d);
                break;
            case 2:
                maximas.add(45000000000d);
                maximas.add(80000000000d);
                break;
            case 3:
                maximas.add(15000000000d);
                maximas.add(30000000000d);
                maximas.add(65000000000d);
                break;
            case 4:
                maximas.add(5000000000d);
                maximas.add(60000000000d);
                break;
            case 5:
                maximas.add(15000000000d);
                maximas.add(30000000000d);
                break;
            case 6:
                maximas.add(5000000000d);
                maximas.add(10000000000d);
                maximas.add(85000000000d);
                break;
            default:
                return null;
        }
        
        
        return maximas;
    }
    
    /**
     * Get all peers/instances from zookeeper and return them in the 
     * ResourceList format
     * Peers/instances will be fetched by the AbstractBioService.getPeers()
     * method
     * 
     * @return 
     */
    public ResourceList getCurrentResourceList () {
        ResourceList resources = new ResourceList();
        
        for (Map.Entry<String, PluginInfo> peer : getPeers().entrySet()) {
            Resource r = new Resource(peer.getValue().getId(),
                        peer.getValue().getFactoryFrequencyCore(),
                        peer.getValue().getCostPerHour(this));
            resources.resources.add(r);
        }
        
        return resources;
    }
    
    /**
     *  Add a service to zookeeper, thereby, generating the full history 
     * structure for given service.
     * 
     *  The service can have a history mode, and, having it, the modes will be 
     * added to the service zookeeper file even without a history. These modes
     * will be removed when the history is big enough to make its own modes.
     * 
     *  The preset modes feature should only be used for testing.
     * 
     * @param service Service to be added
     */
    public void addServiceToZookeeper (PluginService service) {
        // create father node
        cms.createZNode(CreateMode.PERSISTENT, cms.getPath().PREFIX_SERVICE.getFullPath(String.valueOf(service.getId())), service.toString());
        
        // create history structure
        cms.createZNode(CreateMode.PERSISTENT, cms.getPath().HISTORY.getFullPath(String.valueOf(service.getId())), null);
        cms.createZNode(CreateMode.PERSISTENT, cms.getPath().STEP.getFullPath(String.valueOf(service.getId())), service.getHistoryStep().toString());
        
        if (!service.getHistoryMode().isEmpty()) {
            // if there were preset modes they will be added as preset modes
            cms.createZNode(CreateMode.PERSISTENT, cms.getPath().MODES.getFullPath(String.valueOf(service.getId())), service.getHistoryMode().toString());
            
            // this flag should be removed once the history has enough data to 
            //    produce new modes, along with the removal of the preset modes
            cms.createZNode(CreateMode.PERSISTENT, cms.getPath().PRESET.getFullPath(String.valueOf(service.getId())), null);
        } else {
            // otherwise, just create the service with an empty mode list
            cms.createZNode(CreateMode.PERSISTENT, cms.getPath().MODES.getFullPath(String.valueOf(service.getId())), "");
        }
    }
    
    /**
     * @see Appendable
     * @param resource 
     */
    public void addPeerToZookeeper (PluginInfo resource) {
        
    }
    
    @Override
    public void run() {
        // this will be executed periodicaly
        System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
    }

    @Override
    public void start(BioNimbusConfig config, List<Listeners> listeners) {
        Preconditions.checkNotNull(listeners);
        this.config = config;
        this.listeners = listeners;

        listeners.add(this);
    }

    @Override
    public void shutdown() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void getStatus() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void verifyPlugins() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void event(WatchedEvent eventType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
