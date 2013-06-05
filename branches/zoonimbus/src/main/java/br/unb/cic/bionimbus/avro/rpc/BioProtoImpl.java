package br.unb.cic.bionimbus.avro.rpc;

import br.unb.cic.bionimbus.avro.gen.BioProto;
import br.unb.cic.bionimbus.avro.gen.JobCancel;
import br.unb.cic.bionimbus.client.JobInfo;
import br.unb.cic.bionimbus.services.ZooKeeperService;
import br.unb.cic.bionimbus.services.discovery.DiscoveryService;
import br.unb.cic.bionimbus.services.sched.SchedService;
import br.unb.cic.bionimbus.services.storage.StorageService;
import com.google.inject.Inject;
import org.apache.avro.AvroRemoteException;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.*;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.zookeeper.KeeperException;

public class BioProtoImpl implements BioProto {

    private final DiscoveryService discoveryService;
    private final StorageService storageService;
    private final SchedService schedService;
    private final ZooKeeperService zkService;

    @Inject
    public BioProtoImpl(DiscoveryService discoveryService, StorageService storageService, SchedService schedService, ZooKeeperService zkservice) {
        this.discoveryService = discoveryService;
        this.storageService = storageService;
        this.schedService = schedService;
        this.zkService =  zkservice;
    }

    public boolean ping() throws AvroRemoteException {
        return true;
    }

    @Override
    public List<String> listFiles() throws AvroRemoteException {
        File dataFolder = storageService.getDataFolder();
        return Arrays.asList(dataFolder.list());
    }

    @Override
    public List<String> listServices() throws AvroRemoteException {
        //TODO: call storageService
        return asList("blast", "interpro", "bowtie");
    }

    @Override
    public String startJob(String jobID) throws AvroRemoteException {

        JobInfo job = new JobInfo();
        job.setId(null);
        job.setServiceId(Long.parseLong(jobID));

        ArrayList<JobInfo> jobList = new ArrayList<JobInfo>();
        jobList.add(job);
        
        schedService.getPolicy().schedule(jobList, zkService);
        return "Job Executado";
    }

    @Override
    public String cancelJob(String jobID) throws AvroRemoteException {
        //TODO: call schedService
        return "OK";
    }

    @Override
    public List<String> getPeers() throws AvroRemoteException {
        List<String> peers = null;
        List<String> peersOnline = new ArrayList<String>();
        final String ROOT_PEERS = "/peers";
        final String SEPARATOR = "/";
        final String STATUS = "/STATUS";

        try {
            peers = zkService.getChildren(ROOT_PEERS, null);
            for(String peer : peers){
                if(zkService.getZNodeExist(ROOT_PEERS+SEPARATOR+peer+STATUS, false)){
                    peersOnline.add(peer);
                }
            }
        } catch (KeeperException ex) {
            Logger.getLogger(BioProtoImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(BioProtoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
       return peersOnline;
    }

}
