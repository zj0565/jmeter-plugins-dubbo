/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.ningyu.jmeter.plugin.dubbo.gui;

import io.github.ningyu.jmeter.plugin.dubbo.sample.MethodArgument;
import io.github.ningyu.jmeter.plugin.dubbo.sample.ProviderService;
import io.github.ningyu.jmeter.plugin.util.Constants;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.common.URL;
import org.apache.jmeter.gui.util.HorizontalPanel;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.testelement.TestElement;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * DubboCommonPanel </br>
 */
public class DubboCommonPanel {
    private JComboBox<String> registryProtocolText;
    private JComboBox<String> rpcProtocolText;
    private JTextField addressText;
    private JTextField registryGroupText;
    private JTextField timeoutText;
    private JTextField versionText;
    private JTextField retriesText;
    private JTextField clusterText;
    private JTextField interfaceText;
    private JTextField methodText;
    private JTextField groupText;
    private JTextField connectionsText;
    private JComboBox<String> loadbalanceText;
    private JComboBox<String> asyncText;
    private DefaultTableModel model;
    private String[] columnNames = {"paramType", "paramValue"};
    private String[] tmpRow = {"", ""};
    private int textColumns = 2;
    private JAutoCompleteComboBox<String> interfaceList;
    private JAutoCompleteComboBox<String> methodList;
    private TestElement element;

    public void bundleElement(TestElement element) {
        this.element = element;
    }

    public JPanel drawRegistrySettingsPanel() {
        //Registry Settings
        JPanel registrySettings = new VerticalPanel();
        registrySettings.setBorder(BorderFactory.createTitledBorder("Registry Settings"));
        //Protocol
        JPanel ph = new HorizontalPanel();
        JLabel protocolLable = new JLabel("Protocol:", SwingConstants.RIGHT);
        registryProtocolText = new JComboBox<String>(new String[]{"","none", "zookeeper", "multicast", "redis", "simple"});
        registryProtocolText.setToolTipText("\"none\" is direct connection");
        protocolLable.setLabelFor(registryProtocolText);
        ph.add(protocolLable);
        ph.add(registryProtocolText);
        ph.add(makeHelper("Registry center address protocol, The 'none' is direct connection. "));
        registrySettings.add(ph);
        //Address
        JPanel ah = new HorizontalPanel();
        JLabel addressLable = new JLabel("Address:", SwingConstants.RIGHT);
        addressText = new JTextField(textColumns);
        addressLable.setLabelFor(addressText);
        ah.add(addressLable);
        ah.add(addressText);
        ah.add(makeHelper("Use the registry to allow multiple addresses, Use direct connection to allow only one address! Multiple address format: ip1:port1,ip2:port2 . Direct address format: ip:port . "));
        JLabel registryGroupLable = new JLabel("Group:", SwingConstants.RIGHT);
        registryGroupText = new JTextField();
        registryGroupLable.setLabelFor(registryGroupText);
        ah.add(registryGroupLable);
        ah.add(registryGroupText);
        ah.add(makeHelper("Service registration grouping, cross-group services will not affect each other, and can not be called each other, suitable for environmental isolation."));
        registrySettings.add(ah);
        return registrySettings;
    }

    public JPanel drawProtocolSettingsPanel() {
        //RPC Protocol Settings
        JPanel protocolSettings = new VerticalPanel();
        protocolSettings.setBorder(BorderFactory.createTitledBorder("RPC Protocol Settings"));
        //RPC Protocol
        JPanel rpcPh = new HorizontalPanel();
        JLabel rpcProtocolLable = new JLabel("Protocol:", SwingConstants.RIGHT);
        rpcProtocolText = new JComboBox<String>(new String[]{"dubbo://", "rmi://", "hessian://", "webservice://", "memcached://", "redis://"});
        rpcProtocolLable.setLabelFor(rpcProtocolText);
        rpcPh.add(rpcProtocolLable);
        rpcPh.add(rpcProtocolText);
        rpcPh.add(makeHelper("RPC protocol name."));
        protocolSettings.add(rpcPh);
        return protocolSettings;
    }

    public JPanel drawConsumerSettingsPanel() {
        //Consumer Settings
        JPanel consumerSettings = new VerticalPanel();
        consumerSettings.setBorder(BorderFactory.createTitledBorder("Consumer&Service Settings"));
        JPanel h = new HorizontalPanel();
        //Timeout
        JLabel timeoutLable = new JLabel(" Timeout:", SwingConstants.RIGHT);
        timeoutText = new JTextField(textColumns);
        timeoutText.setText(Constants.DEFAULT_TIMEOUT);
        timeoutLable.setLabelFor(timeoutText);
        h.add(timeoutLable);
        h.add(timeoutText);
        h.add(makeHelper("Invoking timeout(ms)"));
        //Version
        JLabel versionLable = new JLabel("Version:", SwingConstants.RIGHT);
        versionText = new JTextField(textColumns);
        versionText.setText(Constants.DEFAULT_VERSION);
        versionLable.setLabelFor(versionText);
        h.add(versionLable);
        h.add(versionText);
        h.add(makeHelper("Service version."));
        //Retries
        JLabel retriesLable = new JLabel("Retries:", SwingConstants.RIGHT);
        retriesText = new JTextField(textColumns);
        retriesText.setText(Constants.DEFAULT_RETRIES);
        retriesLable.setLabelFor(retriesText);
        h.add(retriesLable);
        h.add(retriesText);
        h.add(makeHelper("The retry count for RPC, not including the first invoke. Please set it to 0 if don't need to retry."));
        //Cluster
        JLabel clusterLable = new JLabel("Cluster:", SwingConstants.RIGHT);
        clusterText = new JTextField(textColumns);
        clusterText.setText(Constants.DEFAULT_CLUSTER);
        clusterLable.setLabelFor(clusterText);
        h.add(clusterLable);
        h.add(clusterText);
        h.add(makeHelper("failover/failfast/failsafe/failback/forking are available."));
        //Group
        JLabel groupLable = new JLabel("Group:", SwingConstants.RIGHT);
        groupText = new JTextField(textColumns);
        groupLable.setLabelFor(groupText);
        h.add(groupLable);
        h.add(groupText);
        h.add(makeHelper("The group of the service providers. It can distinguish services when it has multiple implements."));
        //Connections
        JLabel connectionsLable = new JLabel("Connections:", SwingConstants.RIGHT);
        connectionsText = new JTextField(textColumns);
        connectionsText.setText(Constants.DEFAULT_CONNECTIONS);
        connectionsLable.setLabelFor(connectionsText);
        h.add(connectionsLable);
        h.add(connectionsText);
        h.add(makeHelper("The maximum connections of every provider. For short connection such as rmi, http and hessian, it's connection limit, but for long connection such as dubbo, it's connection count."));
        consumerSettings.add(h);

        JPanel hp1 = new HorizontalPanel();
        //Async
        JLabel asyncLable = new JLabel("     Async:", SwingConstants.RIGHT);
        asyncText = new JComboBox<String>(new String[]{"sync", "async"});
        asyncLable.setLabelFor(asyncText);
        hp1.add(asyncLable);
        hp1.add(asyncText);
        hp1.add(makeHelper("Asynchronous execution, not reliable. It does not block the execution thread just only ignores the return value."));
        //Loadbalance
        JLabel loadbalanceLable = new JLabel("Loadbalance:", SwingConstants.RIGHT);
        loadbalanceText = new JComboBox<String>(new String[]{"random", "roundrobin", "leastactive", "consistenthash"});
        loadbalanceLable.setLabelFor(loadbalanceText);
        hp1.add(loadbalanceLable);
        hp1.add(loadbalanceText);
        hp1.add(makeHelper("Strategy of load balance, random, roundrobin and leastactive are available."));
        consumerSettings.add(hp1);
        return consumerSettings;
    }

    public JPanel drawInterfaceSettingsPanel() {
        //Interface Settings
        JPanel interfaceSettings = new VerticalPanel();
        interfaceSettings.setBorder(BorderFactory.createTitledBorder("Interface Settings"));
        //Selection Interface
        JPanel sh = new HorizontalPanel();
        JButton jButton = new JButton("Get Provider List");
        interfaceList = new JAutoCompleteComboBox<String>(new DefaultComboBoxModel<String>(new String[]{}), new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    doChange(e.getItem().toString());
                }
            }
        });
        interfaceList.addPropertyChangeListener("model", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (interfaceList.getSelectedItem() != null) {
                    doChange(interfaceList.getSelectedItem().toString());
                } else {
                    methodList.setModel(new DefaultComboBoxModel<String>(new String[]{}));
                }
            }
        });
        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doConfirm(e, interfaceList, interfaceSettings);
            }
        });
        sh.add(jButton);
        sh.add(new JLabel("Interfaces:", SwingConstants.RIGHT));
        sh.add(interfaceList);
        sh.add(new JLabel("Methods:", SwingConstants.RIGHT));
        methodList = new JAutoCompleteComboBox<String>(new DefaultComboBoxModel<String>(new String[]{}), new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    methodText.setText(e.getItem().toString());
                }
            }
        });
        methodList.addPropertyChangeListener("model", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (methodList.getSelectedItem() != null) {
                    methodText.setText(methodList.getSelectedItem().toString());
                }
            }
        });
        sh.add(methodList);
        interfaceSettings.add(sh);
        //Interface
        JPanel ih = new HorizontalPanel();
        JLabel interfaceLable = new JLabel("Interface:", SwingConstants.RIGHT);
        interfaceText = new JTextField(textColumns);
        interfaceLable.setLabelFor(interfaceText);
        ih.add(interfaceLable);
        ih.add(interfaceText);
        ih.add(makeHelper("The service interface name."));
        interfaceSettings.add(ih);
        //Method
        JPanel mh = new HorizontalPanel();
        JLabel methodLable = new JLabel("   Method:", SwingConstants.RIGHT);
        methodText = new JTextField(textColumns);
        methodLable.setLabelFor(methodText);
        mh.add(methodLable);
        mh.add(methodText);
        mh.add(makeHelper("The service method name"));
        interfaceSettings.add(mh);

        //表格panel
        JPanel tablePanel = new HorizontalPanel();
        //Args
        JLabel argsLable = new JLabel("        Args:", SwingConstants.RIGHT);
        model = new DefaultTableModel();
//        model.setDataVector(new String[][]{{"", ""}}, columnNames);
        model.setDataVector(null, columnNames);
        final JTable table = new JTable(model);
        table.setRowHeight(40);
        //失去光标退出编辑
        table.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        //添加按钮
        JButton addBtn = new JButton("增加");
        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                model.addRow(tmpRow);
            }
        });
        JButton delBtn = new JButton("删除");
        delBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                int rowIndex = table.getSelectedRow();
                if(rowIndex != -1) {
                    model.removeRow(rowIndex);
                }
            }
        });
        //表格滚动条
        JScrollPane scrollpane = new JScrollPane(table);
        tablePanel.add(argsLable);
        tablePanel.add(scrollpane);
        tablePanel.add(addBtn);
        tablePanel.add(delBtn);
        interfaceSettings.add(tablePanel);
        return interfaceSettings;
    }

    public void configureRegistry(TestElement element) {
        registryProtocolText.setSelectedItem(Constants.getRegistryProtocol(element));
        addressText.setText(Constants.getAddress(element));
        registryGroupText.setText(Constants.getRegistryGroup(element));
    }

    public void configureProtocol(TestElement element) {
        rpcProtocolText.setSelectedItem(Constants.getRpcProtocol(element));
    }

    public void configureConsumer(TestElement element) {
        versionText.setText(Constants.getVersion(element));
        timeoutText.setText(Constants.getTimeout(element));
        retriesText.setText(Constants.getRetries(element));
        groupText.setText(Constants.getGroup(element));
        connectionsText.setText(Constants.getConnections(element));
        loadbalanceText.setSelectedItem(Constants.getLoadbalance(element));
        asyncText.setSelectedItem(Constants.getAsync(element));
        clusterText.setText(Constants.getCluster(element));
    }

    public void configureInterface(TestElement element) {
        interfaceText.setText(Constants.getInterface(element));
        methodText.setText(Constants.getMethod(element));
        Vector<String> columnNames = new Vector<String>();
        columnNames.add("paramType");
        columnNames.add("paramValue");
        model.setDataVector(paserMethodArgsData(Constants.getMethodArgs(element)), columnNames);
    }

    public void modifyRegistry(TestElement element) {
        Constants.setRegistryProtocol(registryProtocolText.getSelectedItem().toString(), element);
        Constants.setRegistryGroup(registryGroupText.getText(), element);
        Constants.setAddress(addressText.getText(), element);
    }
    public void modifyProtocol(TestElement element) {
        Constants.setRpcProtocol(rpcProtocolText.getSelectedItem().toString(), element);
    }
    public void modifyConsumer(TestElement element) {
        Constants.setTimeout(timeoutText.getText(), element);
        Constants.setVersion(versionText.getText(), element);
        Constants.setRetries(retriesText.getText(), element);
        Constants.setGroup(groupText.getText(), element);
        Constants.setConnections(connectionsText.getText(), element);
        Constants.setLoadbalance(loadbalanceText.getSelectedItem().toString(), element);
        Constants.setAsync(asyncText.getSelectedItem().toString(), element);
        Constants.setCluster(clusterText.getText(), element);
    }
    public void modifyInterface(TestElement element) {
        Constants.setInterfaceName(interfaceText.getText(), element);
        Constants.setMethod(methodText.getText(), element);
        Constants.setMethodArgs(getMethodArgsData(model.getDataVector()), element);
    }
    public void clearRegistry() {
        registryProtocolText.setSelectedIndex(0);
        registryGroupText.setText("");
        addressText.setText("");
    }
    public void clearProtocol() {
        rpcProtocolText.setSelectedIndex(0);
    }
    public void clearConsumer() {
        timeoutText.setText(Constants.DEFAULT_TIMEOUT);
        versionText.setText(Constants.DEFAULT_VERSION);
        retriesText.setText(Constants.DEFAULT_RETRIES);
        clusterText.setText(Constants.DEFAULT_CLUSTER);
        groupText.setText("");
        connectionsText.setText(Constants.DEFAULT_CONNECTIONS);
        loadbalanceText.setSelectedIndex(0);
        asyncText.setSelectedIndex(0);
    }
    public void clearInterface() {
        interfaceText.setText("");
        methodText.setText("");
        model.setDataVector(null, columnNames);
    }

    private List<MethodArgument> getMethodArgsData(Vector<Vector<String>> data) {
        List<MethodArgument> params = new ArrayList<MethodArgument>();
        if (!data.isEmpty()) {
            //处理参数
            Iterator<Vector<String>> it = data.iterator();
            while(it.hasNext()) {
                Vector<String> param = it.next();
                if (!param.isEmpty()) {
                    params.add(new MethodArgument(param.get(0), param.get(1)));
                }
            }
        }
        return params;
    }

    private Vector<Vector<String>> paserMethodArgsData(List<MethodArgument> list) {
        Vector<Vector<String>> res = new Vector<Vector<String>>();
        for (MethodArgument args : list) {
            Vector<String> v = new Vector<String>();
            v.add(args.getParamType());
            v.add(args.getParamValue());
            res.add(v);
        }
        return res;
    }

    private void doChange(String key) {
        String address = (StringUtils.isBlank(Constants.DEFAULT_PANEL_ADDRESS) ? addressText.getText() : Constants.DEFAULT_PANEL_ADDRESS);
        ProviderService providerService = ProviderService.get(address);
        Map<String, URL> provider = providerService.findByService(key);
        if (provider != null && !provider.isEmpty()) {
            URL url = new ArrayList<URL>(provider.values()).get(0);
            String group = url.getParameter(com.alibaba.dubbo.common.Constants.GROUP_KEY);
            String version = url.getParameter(com.alibaba.dubbo.common.Constants.VERSION_KEY);
            String timeout = url.getParameter(com.alibaba.dubbo.common.Constants.TIMEOUT_KEY);
            String protocol = url.getProtocol() + "://";
            String interfaceName = url.getServiceInterface();
            String method = url.getParameter(com.alibaba.dubbo.common.Constants.METHODS_KEY);
            groupText.setText(group);
            versionText.setText(version);
            timeoutText.setText(timeout);
            rpcProtocolText.setSelectedItem(protocol);
            interfaceText.setText(interfaceName);
            //set method
            String[] items = method.split(",");
            methodList.setModel(new DefaultComboBoxModel<String>(items));
        } else {
            methodList.setModel(new DefaultComboBoxModel<String>(new String[]{}));
        }
    }

    private void doConfirm(ActionEvent event, JAutoCompleteComboBox<String> interfaceList, JPanel gridPanel) {
        String protocol = StringUtils.isBlank(Constants.DEFAULT_PANEL_PROTOCOLS) ? registryProtocolText.getSelectedItem().toString() : Constants.DEFAULT_PANEL_PROTOCOLS;
        String address = StringUtils.isBlank(Constants.DEFAULT_PANEL_ADDRESS) ? addressText.getText() : Constants.DEFAULT_PANEL_ADDRESS;
        String group = StringUtils.isBlank(Constants.DEFAULT_PANEL_GROUP) ? registryGroupText.getText() : Constants.DEFAULT_PANEL_GROUP;
        if (StringUtils.isBlank(address)) {
            JOptionPane.showMessageDialog(gridPanel.getParent(), "Address can't be empty!", "error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int result = JOptionPane.showConfirmDialog(gridPanel.getParent(), "Obtaining all the providers lists may cause jmeter to stop responding for a few seconds. Do you want to continue?", "warn", JOptionPane.YES_NO_CANCEL_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            List<String> list = new ArrayList<String>();
            try {
                list = ProviderService.get(address).getProviders(protocol, address, group);
                JOptionPane.showMessageDialog(gridPanel.getParent(), "Get provider list to finish! Check if the log has errors.", "info", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(gridPanel.getParent(), e.getMessage(), "error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String[] items = list.toArray(new String[]{});
            interfaceList.setModel(new DefaultComboBoxModel<String>(items));
        }
    }

    public JLabel makeHelper(String tooltip) {
        JLabel helpLable = new JLabel();
        helpLable.setIcon(new ImageIcon(getClass().getResource("/images/help.png")));
        helpLable.setToolTipText(tooltip);
        return helpLable;
    }
}